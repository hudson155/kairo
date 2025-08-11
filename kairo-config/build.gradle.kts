plugins {
  id("kairo")
  id("kairo-publish")
  id("kairo-serialization")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.add("-opt-in=kotlinx.serialization.ExperimentalSerializationApi")
  }
}

tasks.test {
  environment("KAIRO_CONFIG_TEST_ENVIRONMENT_VARIABLE_REQUIRED", "VALUE_FROM_ENV")
}

dependencies {
  implementation(project(":bom"))

  // api(project(":kairo-command-runner"))
  // api(project(":kairo-environment-variable-supplier"))
  // api(project(":kairo-gcp-secret-supplier"))
  // implementation(project(":kairo-logging"))
  // api(project(":kairo-protected-string")) // Exposed for clients.
  api(project(":kairo-serialization"))
  // implementation(project(":kairo-util"))

  implementation(libs.hocon)
  implementation(libs.kotlinxSerializationHocon)

  // testImplementation(project(":kairo-logging:testing"))
  testImplementation(project(":kairo-testing"))
}
