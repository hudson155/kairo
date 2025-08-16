plugins {
  id("kairo-library")
  id("kairo-library-publish")
  id("kairo-serialization")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.add("-opt-in=kotlinx.serialization.ExperimentalSerializationApi")
  }
}

dependencies {
  implementation(project(":kairo-command-runner"))
  implementation(project(":kairo-environment-variable-supplier"))
  implementation(project(":kairo-gcp-secret-supplier"))
  implementation(project(":kairo-logging"))
  implementation(project(":kairo-util"))

  implementation(libs.hocon)
  implementation(libs.serialization.hocon)

  testImplementation(project(":kairo-command-runner:testing"))
  testImplementation(project(":kairo-environment-variable-supplier:testing"))
  testImplementation(project(":kairo-gcp-secret-supplier:testing"))
  testImplementation(project(":kairo-testing"))
}
