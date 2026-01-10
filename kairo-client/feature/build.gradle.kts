plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-client"))
  implementation(project(":kairo-dependency-injection")) // Optional peer dependency.
  compileOnly(project(":kairo-feature")) // Forced peer dependency.
  implementation(project(":kairo-optional")) // For serialization.
  compileOnly(project(":kairo-serialization")) // Forced peer dependency.
}
