plugins {
    id("soffa.java8")
    id("soffa.maven-publish")
}

dependencies {
    api(project(":soffa-foundation-api"))
    api("com.fasterxml.jackson.core:jackson-annotations:2.13.1")
    api("org.apache.commons:commons-text:1.9")
    api("commons-validator:commons-validator:1.7")
}


