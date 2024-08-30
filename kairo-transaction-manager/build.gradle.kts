plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-dependency-injection"))
  implementation(project(":kairo-logging"))
  implementation(project(":kairo-util"))

  implementation(libs.kotlinxCoroutinesCore)

  testImplementation(project(":kairo-logging:testing"))
  testImplementation(project(":testing"))
}