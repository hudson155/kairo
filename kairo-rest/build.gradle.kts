plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-exception")) // Available for usage.
  implementation(project(":kairo-logging"))
  implementation(project(":kairo-reflect"))
  api(project(":kairo-rest:endpoint"))
  api(project(":kairo-rest:serialization"))
  compileOnly(project(":kairo-serialization")) // Forced peer dependency.

  api(libs.ktorServer) // Available for usage.
  api(libs.ktorServer.auth) // Available for usage.
  compileOnly(libs.ktorServer.authJwt)
  api(libs.ktorServer.sse) // Available for usage.
}
