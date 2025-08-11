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

dependencies {
  implementation(project(":bom"))

  implementation(libs.hocon)
  implementation(libs.kotlinxSerializationHocon)

  testImplementation(project(":kairo-testing"))
}
