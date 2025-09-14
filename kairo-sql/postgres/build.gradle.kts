plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-exception"))
  implementation(project(":kairo-sql"))
  implementation(project(":kairo-util"))

  implementation(libs.postgres.jdbc)
}
