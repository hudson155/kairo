plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  compileOnly(project(":kairo-exception")) // Forced peer dependency.
  implementation(project(":kairo-testing"))
}
