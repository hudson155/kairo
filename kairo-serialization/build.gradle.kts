plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-logging"))
  api(libs.jacksonCore)
  implementation(libs.jacksonDataformatYaml)
  implementation(libs.jacksonDatatypeJdk8)
  api(libs.jacksonModuleKotlin)

  testImplementation(project(":kairo-logging:testing"))
  testImplementation(project(":testing"))
}
