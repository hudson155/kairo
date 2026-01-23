plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-reflect"))
  compileOnly(project(":kairo-serialization"))

  api(libs.hocon)

  testImplementation(project(":kairo-protected-string"))
  testImplementation(project(":kairo-serialization"))
  testImplementation(project(":kairo-testing"))
}

tasks.test {
  environment("KAIRO_TEST_ENVIRONMENT_VARIABLE", "Hello, World!") // Used by [LoadConfigTest].
}
