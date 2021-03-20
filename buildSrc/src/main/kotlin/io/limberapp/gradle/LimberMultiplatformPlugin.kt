package io.limberapp.gradle

import Plugins
import io.limberapp.gradle.task.JsPackageTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

abstract class LimberMultiplatformPlugin : LimberPlugin() {
  enum class SourceSet(val sourceSetName: String) {
    COMMON_MAIN("commonMain"),
    COMMON_TEST("commonTest"),
    JS_MAIN("jsMain"),
    JS_TEST("jsTest"),
    JVM_MAIN("jvmMain"),
    JVM_TEST("jvmTest");
  }

  override val plugins: List<String>
    get() = super.plugins + Plugins.kotlinMultiplatform

  override fun configure(project: Project) {
    super.configure(project)
    configureCompilationTargets(project)
    installDependencies(project)
    JsPackageTask().applyTo(project)
  }

  private fun configureCompilationTargets(project: Project) {
    project.kotlin {
      js(IR) {
        browser {
          binaries.executable()
        }
      }
      jvm()
    }
  }

  private fun installDependencies(project: Project) {
    project.kotlin {
      sourceSets {
        SourceSet.values().forEach { sourceSet ->
          getByName(sourceSet.sourceSetName) {
            dependencies {
              getDependencies(sourceSet).forEach { apply(it) }
            }
          }
        }
      }
    }
  }

  protected open fun getDependencies(sourceSet: SourceSet): List<Dependency> =
      when (sourceSet) {
        SourceSet.COMMON_MAIN -> DEFAULT_COMMON_DEPENDENCIES
        SourceSet.COMMON_TEST -> DEFAULT_COMMON_TEST_DEPENDENCIES
        SourceSet.JS_MAIN -> DEFAULT_JS_DEPENDENCIES
        SourceSet.JS_TEST -> DEFAULT_JS_TEST_DEPENDENCIES
        SourceSet.JVM_MAIN -> DEFAULT_JVM_DEPENDENCIES
        SourceSet.JVM_TEST -> DEFAULT_JVM_TEST_DEPENDENCIES
      }
}

internal val DEFAULT_COMMON_DEPENDENCIES: List<Dependency> = emptyList()

internal val DEFAULT_COMMON_TEST_DEPENDENCIES: List<Dependency> = listOf(
    Dependency.Kotlin(
        configuration = Dependency.Configuration.IMPLEMENTATION,
        dependencyNotation = "test-annotations-common"
    ),
    Dependency.Kotlin(
        configuration = Dependency.Configuration.IMPLEMENTATION,
        dependencyNotation = "test-common"
    )
)

private fun KotlinDependencyHandler.apply(dependency: Dependency) {
  val dependencyNotation: Any = when (dependency) {
    is Dependency.Explicit -> dependency.dependencyNotation
    is Dependency.Kotlin -> kotlin(dependency.dependencyNotation)
    is Dependency.Project -> project(dependency.dependencyNotation)
  }
  when (dependency.configuration) {
    Dependency.Configuration.IMPLEMENTATION -> implementation(dependencyNotation)
    Dependency.Configuration.RUNTIME_ONLY -> runtimeOnly(dependencyNotation)
    else -> error("Unhandled dependency configuration: ${dependency.configuration}.")
  }
}
