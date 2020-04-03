plugins {
    kotlin("jvm")
    id(Plugins.detekt)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation(project(":piper:sql"))
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
}
