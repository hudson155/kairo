plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-reflect"))
  implementation(project(":kairo-serialization"))
  implementation(project(":kairo-util"))

  api(libs.hocon) // Available for usage.

  testImplementation(project(":kairo-protected-string"))
  testImplementation(project(":kairo-testing"))
}

tasks.test {
  environment("KAIRO_TEST_ENVIRONMENT_VARIABLE", "Hello, World!") // Used by [LoadConfigTest].
}
