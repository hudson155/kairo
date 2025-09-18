plugins {
  kotlin("plugin.serialization")
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-dependency-injection"))
  api(project(":kairo-feature"))
  implementation(project(":kairo-serialization"))
  api(project(":kairo-vertex-ai"))

  testImplementation(project(":kairo-testing"))
  testImplementation(project(":kairo-vertex-ai:testing"))

  testImplementation(libs.serialization.json)
}
