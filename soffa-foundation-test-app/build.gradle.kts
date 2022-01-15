plugins {
    id("soffa.java8")
    id("soffa.springboot")
    id("soffa.qa.coverage.l2")
}

dependencies {
    implementation(project(":soffa-foundation-springboot"))
    testImplementation(project(":soffa-foundation-test"))
}
