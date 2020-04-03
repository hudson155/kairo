plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id(Plugins.detekt)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":piper:serialization"))
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
}
