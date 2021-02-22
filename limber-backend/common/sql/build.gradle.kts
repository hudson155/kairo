dependencies {
  implementation(project(":limber-backend:common:config"))
  api(project(":limber-backend:common:module"))
  implementation(project(":limber-backend:common:type-conversion:interface"))
  implementation(project(":limber-multiplatform:permissions"))
  implementation(Dependencies.Sql.flyway)
  implementation(Dependencies.Sql.hikari)
  api(Dependencies.Sql.jdbi3Kotlin)
  implementation(Dependencies.Sql.jdbi3KotlinSqlobject)
  implementation(Dependencies.Sql.jdbi3Postgres)
  implementation(Dependencies.Sql.postgres)
  testImplementation(project(":limber-backend:common:type-conversion:implementation"))
}
