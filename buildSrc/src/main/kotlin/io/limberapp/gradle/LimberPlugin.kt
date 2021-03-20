package io.limberapp.gradle

import Plugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.AbstractTestTask
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

abstract class LimberPlugin : Plugin<Project> {
  final override fun apply(project: Project) {
    applyPlugins(project)
    configure(project)
  }

  protected open val plugins: List<String> = listOf(Plugins.detekt, Plugins.idea)

  private fun applyPlugins(project: Project) {
    plugins.forEach { project.pluginManager.apply(it) }
  }

  open fun configure(project: Project) {
    configureRepositories(project)
    configureCompilation(project)
    configureTesting(project)
    configureDetekt(project)
  }

  private fun configureRepositories(project: Project) {
    project.repositories {
      mavenCentral()
      // https://jfrog.com/blog/into-the-sunset-bintray-jcenter-gocenter-and-chartcenter/.
      jcenter() // JCenter is EOL.
    }
  }

  private fun configureCompilation(project: Project) {
    project.tasks.withType<KotlinJsCompile> {
      kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
      kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.js.ExperimentalJsExport"
    }
    project.tasks.withType<KotlinJvmCompile> {
      kotlinOptions.jvmTarget = "11"
      kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
      kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.ExperimentalStdlibApi"
    }
  }

  private fun configureTesting(project: Project) {
    project.tasks.withType<AbstractTestTask> {
      testLogging {
        events("passed", "skipped", "failed")
      }
    }
    project.tasks.withType<Test> {
      useJUnitPlatform()
    }
  }

  private fun configureDetekt(project: Project) {
    project.detekt {
      config = project.files("${project.rootDir}/.detekt/config.yaml")
      input = project.files(project.file("src").listFiles()?.map { "src/${it.name}/kotlin" })
    }
  }
}
