plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-gcp-secret-supplier"))

  testImplementation(project(":kairo-testing"))
}
