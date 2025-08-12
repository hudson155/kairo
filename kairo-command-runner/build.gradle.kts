plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(platform(project(":bom")))

  implementation(project(":kairo-logging"))
  implementation(project(":kairo-util"))

  testImplementation(project(":kairo-logging:testing"))
  testImplementation(project(":kairo-testing"))
}
