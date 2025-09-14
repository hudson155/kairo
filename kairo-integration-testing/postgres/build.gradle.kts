plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-integration-testing"))
  implementation(project(":kairo-sql:feature"))
  implementation(project(":kairo-sql:postgres"))
  implementation(project(":kairo-testing"))

  implementation(libs.testcontainers.postgres)
}
