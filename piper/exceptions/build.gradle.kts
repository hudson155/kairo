plugins {
    kotlin("jvm")
    id(Plugins.detekt)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
}
