import kairo.buildSrc.Dependencies
import kairo.buildSrc.main

plugins {
  id("kairo")
  id("kairo-publish")
}

main {
  dependencies {
    api(Dependencies.kotlinLoggingJvm)
    implementation(Dependencies.kotlinxCoroutinesSlf4j)
    implementation(Dependencies.log4jConfigYaml)
    implementation(Dependencies.log4jCore)
    implementation(Dependencies.log4jSlf4j2Impl)
  }
}
