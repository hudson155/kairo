plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(libs.ktorHttp) // LogicalFailure exposes [HttpStatusCode].

  testImplementation(project(":kairo-rest:serialization"))
  testImplementation(project(":kairo-serialization"))
  testImplementation(project(":kairo-testing"))
}
