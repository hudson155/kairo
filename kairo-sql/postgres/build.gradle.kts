plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-exception"))
  compileOnly(project(":kairo-sql"))
  implementation(project(":kairo-util"))

  compileOnly(libs.postgres.r2dbc)
}
