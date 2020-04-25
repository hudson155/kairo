plugins {
    kotlin("jvm")
    id(Plugins.detekt)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api(project(":piper:config")) // Uses ConfigString in the interface
    api(project(":piper:module")) // This artifact implements a module
    api(Dependencies.Sql.exposed) // Provides Exposed SQL DSL
    implementation(Dependencies.Sql.flyway)
    api(Dependencies.Sql.hikari) // Uses Hikari in the interface
    api(Dependencies.Sql.jdbi3Kotlin) // Provides JDBI3 interface
    implementation(Dependencies.Sql.jdbi3KotlinSqlobject)
    implementation(Dependencies.Sql.jdbi3Postgres)
    api(Dependencies.Sql.postgres) // This artifact only supports Postgres right now
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
    input = files("src/main/kotlin", "src/test/kotlin")
}
