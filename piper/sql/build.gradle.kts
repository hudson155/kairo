plugins {
    kotlin("jvm")
    id(Plugins.detekt)
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
}
tasks.compileTestKotlin {
    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api(project(":piper:common"))
    api(Dependencies.Sql.exposed)
    api(Dependencies.Sql.flyway)
    api(Dependencies.Sql.hikari)
    api(Dependencies.Sql.postgres)
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
}
