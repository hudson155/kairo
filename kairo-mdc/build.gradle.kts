plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-logging"))
  implementation(project(":kairo-serialization"))

  implementation(libs.kotlinxCoroutinesSlf4j)
  implementation(libs.log4j2Slf4j2Impl)
}
