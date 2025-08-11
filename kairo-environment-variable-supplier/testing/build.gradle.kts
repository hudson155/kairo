plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":bom"))

  api(project(":kairo-environment-variable-supplier"))

  testImplementation(project(":kairo-testing"))
}
