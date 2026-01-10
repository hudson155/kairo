plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-client"))
  implementation(project(":kairo-dependency-injection")) // Optional peer dependency.
  compileOnly(project(":kairo-feature")) // Forced peer dependency.
  compileOnly(project(":kairo-serialization")) // Forced peer dependency.
}
