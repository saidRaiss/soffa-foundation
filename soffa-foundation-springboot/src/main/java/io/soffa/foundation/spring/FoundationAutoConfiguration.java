package io.soffa.foundation.spring;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Configuration
@ComponentScan({"io.soffa.foundation"})
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE - 10)
public class FoundationAutoConfiguration {

}
