plugins {
    kotlin("jvm")
    id(Plugins.detekt)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":limber-backend-application:common:service-interface"))
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
}
