plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-exception")) // Forced peer dependency.
  implementation(project(":kairo-sql"))
  implementation(project(":kairo-util"))

  compileOnly(libs.postgres.jdbc)
}
