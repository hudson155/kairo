plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.add("-Xallow-kotlin-package")
  }
}

dependencies {
  implementation(kotlin("reflect"))

  testImplementation(project(":kairo-testing"))
}
