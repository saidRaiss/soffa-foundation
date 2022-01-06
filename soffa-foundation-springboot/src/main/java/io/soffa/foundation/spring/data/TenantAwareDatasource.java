package io.soffa.foundation.spring.data;

import com.google.common.collect.ImmutableMap;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.soffa.foundation.commons.Logger;
import io.soffa.foundation.commons.TextUtil;
import io.soffa.foundation.context.TenantHolder;
import io.soffa.foundation.core.model.TenantId;
import io.soffa.foundation.data.DataSourceProperties;
import io.soffa.foundation.data.DbMigration;
import io.soffa.foundation.exceptions.DatabaseException;
import io.soffa.foundation.exceptions.TechnicalException;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class TenantAwareDatasource extends AbstractRoutingDataSource implements ApplicationListener<ContextRefreshedEvent> {

    public static final String NONE = "NONE";
    private static final Logger LOG = Logger.get(TenantAwareDatasource.class);
    private final Map<String, Object> dataSources = new ConcurrentHashMap<>();
    private final ResourceLoader resourceLoader = new DefaultResourceLoader();
    private final String tablesPrefix;
    private final String appicationName;
    private boolean appicationStarted;

    @SneakyThrows
    public TenantAwareDatasource(final Map<String, String> links,
                                 final String tablesPrefix,
                                 final String appicationName) {

        super();
        this.tablesPrefix = tablesPrefix;
        this.appicationName = appicationName;
        setLenientFallback(false);
        CustomPhysicalNamingStrategy.tablePrefix = tablesPrefix;
        if (links == null || links.isEmpty()) {
            throw new TechnicalException("No db link provided");
        }
        links.forEach((name, url) -> dataSources.put(name, createDataSource(DataSourceProperties.create(name, url.trim()))));
        dataSources.put(NONE, new MockDataSource());
        TenantHolder.hasDefault = dataSources.containsKey(TenantId.DEFAULT_VALUE);

        super.setTargetDataSources(ImmutableMap.copyOf(dataSources));
    }


    @Override
    protected Object determineCurrentLookupKey() {
        String linkId = TenantHolder.get().orElse(null);
        if (linkId == null) {
            if (dataSources.containsKey(TenantId.DEFAULT_VALUE)) {
                return TenantId.DEFAULT_VALUE;
            } else if (!appicationStarted) {
                return NONE;
            }
            throw new DatabaseException("Missing database link. Don't forget to set active tenant with TenantHolder.set()");
        }
        if (!dataSources.containsKey(linkId)) {
            throw new DatabaseException("%s is not a valid database link", linkId);
        }
        return linkId;
    }

    public DataSource getDefault() {
        boolean hasOneItem = dataSources.size() == 1;
        if (hasOneItem) {
            return (DataSource) dataSources.values().iterator().next();
        }
        if (dataSources.containsKey(TenantId.DEFAULT_VALUE)) {
            return (DataSource) dataSources.get(TenantId.DEFAULT_VALUE);
        }
        throw new TechnicalException("No default datasource registered");
    }

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        appicationStarted = true;
    }

    @SneakyThrows
    private DataSource createDataSource(DataSourceProperties config) {

        HikariConfig hc = new HikariConfig();

        hc.setDriverClassName(config.getDriverClassName());
        hc.setUsername(config.getUsername());
        hc.setPassword(config.getPassword());
        hc.setJdbcUrl(config.getUrl());
        hc.setPoolName(config.getName() + "__" + RandomUtils.nextInt());
        hc.setConnectionTestQuery("select 1");
        hc.setIdleTimeout(30_000);
        hc.setMaximumPoolSize(20);
        hc.setMinimumIdle(5);
        hc.setMaxLifetime(2_000_000);
        hc.setConnectionTimeout(30_000);
        hc.setValidationTimeout(10_000);
        if (config.hasSchema()) {
            hc.setSchema(config.getSchema());
        }
        return new HikariDataSource(hc);
    }

    public void applyMigrations(DbMigration source) {
        for (String changeLogPath : source.getSources()) {
            Resource res = resourceLoader.getResource(changeLogPath);
            if (!res.exists()) {
                throw new TechnicalException("Liquibase changeLog was not found: %s", changeLogPath);
            }
            for (Object value : dataSources.values()) {
                if (value instanceof HikariDataSource) {
                    applyMigrations((DataSource) value, changeLogPath);
                }
            }
        }
    }

    public void applyMigrations(DataSource dataSource, String changeLogPath) {
        SpringLiquibase lqb = new SpringLiquibase();
        lqb.setChangeLog(changeLogPath);
        lqb.setDropFirst(false);
        lqb.setDataSource(dataSource);
        lqb.setResourceLoader(resourceLoader);
        Map<String, String> changeLogParams = new HashMap<>();

        changeLogParams.put("table_prefix", "");
        changeLogParams.put("tablePrefix", "");

        if (TextUtil.isNotEmpty(tablesPrefix)) {
            changeLogParams.put("table_prefix", tablesPrefix + "_");
            changeLogParams.put("tablePrefix", tablesPrefix + "_");

            lqb.setDatabaseChangeLogLockTable(tablesPrefix + "_changelog_lock");
            lqb.setDatabaseChangeLogTable(tablesPrefix + "_changelog");
        }
        if (TextUtil.isNotEmpty(appicationName)) {
            changeLogParams.put("application", appicationName);
            changeLogParams.put("applicationName", appicationName);
            changeLogParams.put("application_name", appicationName);
        }

        doApplyMigration(lqb, changeLogParams, (HikariDataSource) dataSource);
    }

    private void doApplyMigration(SpringLiquibase lqb, Map<String, String> changeLogParams, HikariDataSource ds) {
        String schema = ds.getSchema();
        String dsName = ds.getPoolName().split("__")[0];
        if (TenantId.DEFAULT_VALUE.equals(dsName)) {
            lqb.setContexts(TenantId.DEFAULT_VALUE);
        } else {
            lqb.setContexts("tenant," + dsName.toLowerCase());
        }
        if (TextUtil.isNotEmpty(schema)) {
            lqb.setDefaultSchema(schema);
            lqb.setLiquibaseSchema(schema);
        }
        lqb.setChangeLogParameters(changeLogParams);
        try {
            lqb.afterPropertiesSet(); // Run migrations
            LOG.info("Datasource %s bootstrapped successfully", dsName);
        } catch (LiquibaseException e) {
            throw new DatabaseException(e, "Migration failed for %s", schema);
        }
    }

    public DataSource get(String tenant) {
        return Objects.requireNonNull((DataSource) dataSources.get(tenant));
    }


}
