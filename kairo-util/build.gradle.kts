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
  implementation(kotlin("reflect"))

  implementation(project(":bom"))

  testImplementation(project(":kairo-testing"))
}
