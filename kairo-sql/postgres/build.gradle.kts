plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-exception")) // Optional peer dependency.
  compileOnly(project(":kairo-sql")) // Forced peer dependency.
  implementation(project(":kairo-util"))

  compileOnly(libs.postgres.r2dbc)
}
