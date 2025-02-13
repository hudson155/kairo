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
  implementation(project(":kairo-reflect"))

  testImplementation(project(":kairo-testing"))
}
