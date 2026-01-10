plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-dependency-injection")) // Optional peer dependency.
  compileOnly(project(":kairo-feature")) // Forced peer dependency.
  api(project(":kairo-protected-string")) // In config.
  api(project(":kairo-slack"))
}
