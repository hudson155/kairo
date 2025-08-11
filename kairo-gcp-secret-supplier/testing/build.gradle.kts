plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":bom"))

  api(project(":kairo-gcp-secret-supplier"))

  testImplementation(project(":kairo-testing"))
}
