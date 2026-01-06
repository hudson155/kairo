plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-datetime"))
  implementation(project(":kairo-reflect"))

  api(libs.jackson) // Available for usage.
  implementation(libs.jackson.datatypeJsr310)
  api(libs.jackson.moduleKotlin) // Available for usage.
  implementation(libs.ktorHttp) // Used for [HttpMethod] and [HttpStatusCode].
  api(libs.ktorUtils) // Used for [Attributes].

  testImplementation(project(":kairo-testing"))
}
