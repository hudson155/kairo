plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(platform(project(":bom")))

  api(project(":kairo-gcp-secret-supplier"))

  testImplementation(project(":kairo-testing"))
}
