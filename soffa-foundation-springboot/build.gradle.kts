plugins {
    id("soffa.java8")
    id("soffa.maven-publish")
    id("soffa.springboot.library")
}




dependencies {
    api(project(":soffa-foundation-core"))
    api("org.springframework.boot:spring-boot-starter-web") {
        exclude(module = "spring-boot-starter-tomcat")
    }
    runtimeOnly("org.springframework.boot:spring-boot-starter-undertow")
    implementation(platform("org.apache.logging.log4j:log4j-bom:2.16.0"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    api("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
    implementation("net.logstash.logback:logstash-logback-encoder:7.0.1")
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    api("org.springframework.cloud:spring-cloud-starter-vault-config")
    api("org.springframework.boot:spring-boot-starter-thymeleaf")
    compileOnly("javax.servlet:javax.servlet-api:4.0.1")
    api("com.github.fridujo:rabbitmq-mock:1.1.1")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.postgresql:postgresql:42.3.1")
    // implementation("org.mockito:mockito-core:3.12.4")
    api("org.springdoc:springdoc-openapi-ui:1.5.12")
    implementation("org.springdoc:springdoc-openapi-security:1.5.12")
    testImplementation(project(":soffa-foundation-test"))
}

