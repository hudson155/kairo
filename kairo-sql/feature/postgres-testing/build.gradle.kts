plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-feature:testing"))
  implementation(project(":kairo-sql:feature"))
  implementation(project(":kairo-sql:postgres"))
  implementation(project(":kairo-testing"))

  implementation(libs.exposed.jdbc)
  implementation(libs.testcontainers.postgres)
}
