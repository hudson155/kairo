plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  compileOnly(project(":kairo-serialization")) // Forced peer dependency.

  api(libs.hocon)
}
