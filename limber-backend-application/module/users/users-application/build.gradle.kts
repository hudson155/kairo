plugins {
    kotlin("jvm")
    id(Plugins.detekt)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":limber-backend-application:module:users:users-rest-interface"))
    api(project(":limber-backend-application:module:users:users-service-interface"))
    implementation(project(":limber-backend-application:common"))
    implementation(project(":limber-backend-application:common:interface"))
    implementation(project(":piper:sql"))
    testImplementation(project(":limber-backend-application:common:testing"))
    testImplementation(project(":piper:sql:testing"))
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
    input = files("src/main/kotlin", "src/test/kotlin")
}
