plugins {
    id("soffa.java8")
    id("soffa.maven-publish")
    id("soffa.test.junit5")

}

dependencies {
    api("io.github.openfeign:feign-core:11.6")
    api("javax.validation:validation-api:2.0.1.Final")
    api("javax.inject:javax.inject:1")
    api("javax.annotation:javax.annotation-api:1.3.2")
    api("io.swagger.core.v3:swagger-annotations:2.1.11")
    api("io.swagger.core.v3:swagger-models:2.1.11")
    api("org.apache.commons:commons-lang3:3.12.0")
    api("javax.ws.rs:javax.ws.rs-api:2.1.1")
    api("com.fasterxml.jackson.core:jackson-annotations:2.13.0")
    api("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.0")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.0")
    api("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.13.0")
    api("joda-time:joda-time:2.10.12")
    api("commons-io:commons-io:2.8.0")
    api("com.squareup.okhttp3:okhttp:4.9.2")
    api("com.konghq:unirest-java:3.13.0")
    api("com.google.guava:guava:31.0.1-jre")
    api("com.github.michaelgantman:MgntUtils:1.5.1.2")
    api("com.joestelmach:natty:0.13")
    api("org.json:json:20210307")
}

