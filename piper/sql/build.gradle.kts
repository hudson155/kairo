plugins {
    kotlin("jvm")
    id(Plugins.detekt)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    api(project(":piper:common"))
    api(Dependencies.Sql.exposed)
    api(Dependencies.Sql.flyway)
    api(Dependencies.Sql.hikari)
    api(Dependencies.Sql.postgres)
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
    input = files("src/main/kotlin", "src/test/kotlin")
}
