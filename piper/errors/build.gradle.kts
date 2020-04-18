plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id(Plugins.detekt)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(Dependencies.Serialization.jvm)
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
    input = files("src/main/kotlin", "src/test/kotlin")
}
