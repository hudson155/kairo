plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  compileOnly(project(":kairo-dependency-injection")) // Forced peer dependency.
  compileOnly(project(":kairo-testing")) // Forced peer dependency.
}
