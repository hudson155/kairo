plugins {
  id("limber-jvm-library")
}

dependencies {
  api(project(":limber-backend:common:sql"))
  implementation(Dependencies.Sql.hikari)
}
