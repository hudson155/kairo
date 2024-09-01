plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(libs.kotlinLoggingJvm)
  implementation(libs.log4j2ConfigYaml)
  implementation(libs.log4j2Core)
  implementation(libs.log4j2Slf4j2Impl)
}
