package io.limberapp.gradle.task

import org.gradle.api.Project
import org.gradle.api.Task

/**
 * Copies the outputs of the Kotlin/JS compiler to a common folder for use by JS code.
 */
class JsPackageTask : GradleTask() {
  override val name: String = "jsPackage"

  override val dependencies: List<String> = listOf("jsProductionExecutableCompileSync")

  override val isDependencyOf: List<String> = listOf("assemble")

  override fun Task.run(project: Project) {
    val buildDir = project.buildDir
    val rootBuildDir = project.rootProject.buildDir
    // The combinedName convention is based on observed Kotlin/JS compiler output directories.
    val combinedName = "${project.rootProject.name}-${project.name}"
    val sourceDir = "$rootBuildDir/js/packages/$combinedName/kotlin"
    val packageJsonFile = "$buildDir/tmp/jsPublicPackageJson/package.json"
    val outputDir = "$buildDir/js-package"

    inputs.dir(sourceDir)
    inputs.file(packageJsonFile)
    outputs.dir(outputDir)

    doLast {
      project.copy {
        from(sourceDir, packageJsonFile)
        into(outputDir)
      }
    }
  }
}
