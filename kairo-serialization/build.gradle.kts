plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-logging"))
  implementation(project(":kairo-money"))
  implementation(project(":kairo-reflect"))

  api(libs.jacksonCore) // Exposed for clients.
  api(libs.jacksonDataformatXml)
  api(libs.jacksonDataformatYaml)
  implementation(libs.jacksonDatatypeJdk8)
  api(libs.jacksonModuleKogera) // There are some extension functions to expose.
  implementation(libs.ktorHttpJvm)

  testImplementation(project(":kairo-logging:testing"))
  testImplementation(project(":kairo-testing"))
}
