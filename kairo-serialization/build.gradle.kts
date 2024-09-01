plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-logging"))

  api(libs.jacksonCore)
  implementation(libs.jacksonDataformatYaml)
  implementation(libs.jacksonDatatypeJdk8)
  api(libs.jacksonModuleKotlin) // There are some extension functions to expose.

  testImplementation(project(":kairo-logging:testing"))
  testImplementation(project(":kairo-testing"))
}
