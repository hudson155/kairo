plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-dependency-injection")) // Optional peer dependency.
  compileOnly(project(":kairo-feature")) // Forced peer dependency.
  implementation(project(":kairo-protected-string")) // In config.
  implementation(project(":kairo-serialization"))
  api(project(":kairo-sql"))

  api(libs.r2dbc.pool)
}
