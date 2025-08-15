plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-gcp-secret-supplier"))

  testImplementation(project(":kairo-testing"))
}
