plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":bom"))

  api(libs.kotlinLoggingJvm)
  implementation(libs.log4jConfigYaml)
  implementation(libs.log4jCore)
  implementation(libs.log4jLayoutTemplateJson)
  implementation(libs.log4jSlf4j2Impl)
}
