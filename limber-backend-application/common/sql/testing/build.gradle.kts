plugins {
    kotlin("jvm")
    id(Plugins.detekt)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api(project(":limber-backend-application:common:sql"))
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
    input = files("src/main/kotlin", "src/test/kotlin")
}
