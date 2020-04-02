import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id(Plugins.detekt)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api(project(":limber-backend-application:module:auth:service-interface"))
    implementation(project(":limber-backend-application:common"))
    implementation(project(":limber-backend-application:common:service-interface"))
    implementation(project(":limber-backend-application:module:orgs:service-interface"))
    implementation(project(":limber-backend-application:module:users:service-interface"))
    implementation(project(":piper:sql"))
    implementation(Dependencies.Bcrypt.jbcrypt)
    testImplementation(project(":limber-backend-application:common:testing"))
    testImplementation(project(":piper:sql:testing"))
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
}
