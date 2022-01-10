package io.soffa.foundation.spring;

import io.soffa.foundation.data.entities.SysLogEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;

@ConditionalOnProperty(value = "app.sys-logs.enabled", havingValue = "true")
public interface SysLogJpaRepository extends JpaRepository<SysLogEntity, String> {
}
