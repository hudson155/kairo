plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-exception"))
  implementation(project(":kairo-logging"))
  api(project(":kairo-optional")) // Available for usage in reps.
  implementation(project(":kairo-reflect"))
  api(project(":kairo-rest:serialization"))
  implementation(project(":kairo-serialization"))
  implementation(project(":kairo-util"))

  api(libs.ktorServer) // Available for usage.
  api(libs.ktorServer.auth) // Available for usage.
  compileOnly(libs.ktorServer.authJwt)
  api(libs.ktorServer.sse) // Available for usage.

  testImplementation(project(":kairo-id"))
  testImplementation(project(":kairo-testing"))
}
