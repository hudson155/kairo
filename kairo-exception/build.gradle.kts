plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.add("-opt-in=kotlinx.serialization.ExperimentalSerializationApi") // JsonPrimitive.
  }
}

dependencies {
  api(project(":kairo-serialization")) // LogicalFailure exposes JsonElement.

  api(libs.ktorHttp) // LogicalFailure exposes HttpStatusCode.
  api(libs.serialization.json) // LogicalFailure exposes JsonElement.

  testImplementation(project(":kairo-testing"))
}
