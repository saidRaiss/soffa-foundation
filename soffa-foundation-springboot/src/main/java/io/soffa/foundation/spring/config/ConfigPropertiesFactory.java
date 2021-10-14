package io.soffa.foundation.spring.config;

import io.soffa.foundation.s3.S3config;
import io.soffa.foundation.support.mail.adapters.MailerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigPropertiesFactory {

    @Bean
    @ConditionalOnProperty(value = "app.s3.enabled", havingValue = "true")
    @ConfigurationProperties(prefix = "app.s3")
    public S3config createS3Config() {
        return new S3config();
    }


    @Bean
    @ConfigurationProperties(prefix = "app.mail")
    @ConditionalOnProperty(value = "app.mail.provider", havingValue = "smtp")
    public MailerConfig createSmtpConfig() {
        return new MailerConfig();
    }


}
