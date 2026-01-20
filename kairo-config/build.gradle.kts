plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-hocon"))
  implementation(project(":kairo-reflect"))
  implementation(project(":kairo-serialization"))
  implementation(project(":kairo-util"))

  testImplementation(project(":kairo-protected-string"))
  testImplementation(project(":kairo-testing"))
}

tasks.test {
  environment("KAIRO_TEST_ENVIRONMENT_VARIABLE", "Hello, World!") // Used by [LoadConfigTest].
}
