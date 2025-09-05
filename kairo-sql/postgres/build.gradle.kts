plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-sql"))

  implementation(libs.postgres.r2dbc)
}
