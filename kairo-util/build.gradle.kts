plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

kotlin {
  compilerOptions {
    /**
     * This allows us to define helper functions that don't need to be explicitly imported.
     * The kotlin package should be used sparingly.
     */
    freeCompilerArgs.add("-Xallow-kotlin-package")
  }
}

dependencies {
  compileOnly(libs.guava)

  testImplementation(project(":kairo-testing"))
}
