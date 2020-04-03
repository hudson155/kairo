plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id(Plugins.detekt)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    api(project(":limber-backend-application:common:service-interface"))
    api(project(":piper:common"))
    implementation(project(":piper:serialization"))
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
}
