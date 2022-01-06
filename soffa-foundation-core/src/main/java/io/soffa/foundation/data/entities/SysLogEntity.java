package io.soffa.foundation.data.entities;

import io.soffa.foundation.commons.IdGenerator;
import io.soffa.foundation.commons.TextUtil;
import io.soffa.foundation.data.SysLog;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sys_logs")
@Data
public class SysLogEntity {

    @Id
    private String id;
    private String kind;
    private String event;
    private String data;
    @Column(name = "value_num")
    private Float value;
    private String requestId;
    private String spanId;
    private String traceId;
    private String userId;
    private String application;
    private long duration;
    private String error;
    private String errorDetails;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @SneakyThrows
    public static SysLogEntity fromDomain(SysLog model) {
        SysLogEntity entity = new SysLogEntity();
        BeanUtils.copyProperties(entity, model);
        return entity;
    }

    @PrePersist
    public void onPrePersist() {
        if (TextUtil.isEmpty(id)) {
            id = IdGenerator.shortUUID("slog_");
        }
        if (createdAt == null) {
            createdAt = new Date();
        }
    }

    @SneakyThrows
    public SysLog toDomain() {
        SysLog domain = new SysLog();
        BeanUtils.copyProperties(domain, this);
        return domain;
    }
}
