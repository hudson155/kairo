plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-dependency-injection"))
  compileOnly(project(":kairo-feature"))
  api(project(":kairo-protected-string")) // In config.
  api(project(":kairo-sql"))

  api(libs.r2dbc.pool)
}
