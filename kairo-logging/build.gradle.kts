plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(libs.kotlinLoggingJvm)
  implementation(libs.log4jConfigYaml)
  implementation(libs.log4jCore)
  implementation(libs.log4jLayoutTemplateJson)
  implementation(libs.log4jSlf4j2Impl)
}
