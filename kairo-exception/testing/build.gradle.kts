plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  compileOnly(project(":kairo-exception")) // Forced peer dependency.
  compileOnly(project(":kairo-testing")) // Forced peer dependency.

  testImplementation(project(":kairo-exception"))
  testImplementation(project(":kairo-testing"))
}
