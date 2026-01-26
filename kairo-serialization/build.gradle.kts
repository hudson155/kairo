plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-datetime"))
  implementation(project(":kairo-reflect"))

  api(libs.jackson)
  implementation(libs.jackson.datatypeJsr310)
  api(libs.jackson.moduleKotlin)
  api(libs.ktorUtils) // For [Attributes].

  testImplementation(project(":kairo-testing"))
}
