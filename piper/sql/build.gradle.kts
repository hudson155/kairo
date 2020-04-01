dependencies {
    api(project(":piper:common"))
    api(Dependencies.Sql.exposed)
    api(Dependencies.Sql.flyway)
    api(Dependencies.Sql.hikari)
    api(Dependencies.Sql.postgres)
}
