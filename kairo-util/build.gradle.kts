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

  api(platform(project(":bom")))

  testImplementation(project(":kairo-testing"))
}
