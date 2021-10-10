plugins {
    id("soffa.java8")
    id("soffa.maven-publish")
    id("soffa.test.junit5")

}

dependencies {
    api(project(":soffa-foundation-api"))

    api("joda-time:joda-time:2.10.12")
    implementation("com.joestelmach:natty:0.13")
    implementation("com.aventrix.jnanoid:jnanoid:2.0.0")

    api("commons-io:commons-io:2.8.0")
    api("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.0")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.0")
    // implementation("commons-beanutils:commons-beanutils:1.9.4")
    implementation("org.json:json:20210307")
    implementation("com.nimbusds:nimbus-jose-jwt:9.15.2")
    implementation("commons-codec:commons-codec:1.15")
    api("com.squareup.okhttp3:okhttp:4.9.2")
    implementation("com.konghq:unirest-java:3.13.0")
    implementation("com.github.michaelgantman:MgntUtils:1.5.1.2")

    api("com.google.guava:guava:31.0.1-jre")

    // api("io.github.resilience4j:resilience4j-all:1.7.1")
    api("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.13.0")
    api("com.jayway.jsonpath:json-path:2.6.0")
    // api("com.github.ben-manes.caffeine:caffeine:3.0.4")
    api("javax.transaction:javax.transaction-api:1.3")
    api("org.hibernate.validator:hibernate-validator-annotation-processor:7.0.1.Final")
    api("org.hibernate.validator:hibernate-validator:7.0.1.Final")
    api("org.checkerframework:checker-qual:3.18.1")
    api("com.amazonaws:aws-java-sdk-s3:1.12.84")
    api("org.hibernate.javax.persistence:hibernate-jpa-2.1-api:1.0.2.Final")
    api("org.jobrunr:jobrunr:4.0.0"){
        exclude(group="com.zaxxer")
    }
    api("org.jdbi:jdbi3-core:3.22.0") {
        exclude(group = "com.github.ben-manes.caffeine")
    }
    api("org.liquibase:liquibase-core:4.4.3")
    api("com.h2database:h2:1.4.200")
    api("commons-beanutils:commons-beanutils:1.9.4")

}

