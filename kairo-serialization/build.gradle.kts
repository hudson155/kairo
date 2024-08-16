plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-logging"))
  api(libs.jacksonCore)
  implementation(libs.jacksonDatatypeJdk8)
  api(libs.jacksonModuleKotlin)

  testImplementation(project(":testing"))
  testImplementation(project(":kairo-logging:testing"))
}
