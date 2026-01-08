plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-serialization"))

  api(libs.ktorHttp) // LogicalFailure exposes [HttpStatusCode].

  testImplementation(project(":kairo-rest:serialization"))
  testImplementation(project(":kairo-testing"))
}
