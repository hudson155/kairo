plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id(Plugins.detekt)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api(project(":limber-backend-application:common:interface"))
    api(project(":piper:common"))
    implementation(project(":piper:serialization"))
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
    input = files("src/main/kotlin", "src/test/kotlin")
}
