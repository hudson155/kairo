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
  environment("KAIRO_CONFIG_SOURCE_TEST", "Hello, World!")
}

dependencies {
  implementation(project(":bom"))

  implementation(project(":kairo-environment-variable-supplier"))
  implementation(project(":kairo-util"))

  implementation(libs.hocon)
  implementation(libs.kotlinxSerializationHocon)

  testImplementation(project(":kairo-testing"))
}
