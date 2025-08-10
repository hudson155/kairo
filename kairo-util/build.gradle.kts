plugins {
  id("kairo")
  id("kairo-publish")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.add("-Xallow-kotlin-package")
  }
}

dependencies {
  testImplementation(project(":kairo-testing"))
}
