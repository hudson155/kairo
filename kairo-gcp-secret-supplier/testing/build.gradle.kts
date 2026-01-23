plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  compileOnly(project(":kairo-gcp-secret-supplier"))

  testImplementation(project(":kairo-gcp-secret-supplier"))
  testImplementation(project(":kairo-testing"))
}
