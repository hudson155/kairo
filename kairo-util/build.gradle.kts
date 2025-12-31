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
  /**
   * Guava is a large library.
   * We only include it for compilation so that if a project isn't using it at runtime,
   * kairo-util doesn't pull it in unnecessarily.
   */
  compileOnly(libs.guava)

  testImplementation(project(":kairo-testing"))
}
