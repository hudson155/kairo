plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-logging"))
  implementation(project(":kairo-money"))

  api(libs.jacksonCore) // Exposed for clients.
  api(libs.jacksonDataformatXml)
  api(libs.jacksonDataformatYaml)
  implementation(libs.jacksonDatatypeJdk8)
  api(libs.jacksonModuleKotlin) // There are some extension functions to expose.

  testImplementation(project(":kairo-logging:testing"))
  testImplementation(project(":kairo-testing"))
}
