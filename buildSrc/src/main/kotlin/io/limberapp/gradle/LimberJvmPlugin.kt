package io.limberapp.gradle

import Dependencies
import Plugins
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.project

abstract class LimberJvmPlugin : LimberPlugin() {
  override val plugins: List<String>
    get() = super.plugins + Plugins.kotlinJvm

  override fun configure(project: Project) {
    super.configure(project)
    installDependencies(project)
    configureJar(project)
  }

  private fun installDependencies(project: Project) {
    project.dependencies {
      getDependencies().forEach { apply(it) }
    }
  }

  protected open fun getDependencies(): List<Dependency> =
      DEFAULT_JVM_DEPENDENCIES + DEFAULT_JVM_TEST_DEPENDENCIES.test()

  private fun configureJar(project: Project) {
    project.tasks.withType(Jar::class.java) {
      // Archives (JARs) are named using the fully qualified project path in order to avoid
      // collisions when multiple JARs are combined to form an application.
      archiveBaseName.set(project.path.drop(1).replace(':', '-'))
    }
  }
}

internal val DEFAULT_JVM_DEPENDENCIES: List<Dependency> = listOf(
    Dependency.Kotlin(
        configuration = Dependency.Configuration.IMPLEMENTATION,
        dependencyNotation = "reflect",
    ),
    Dependency.Explicit(
        configuration = Dependency.Configuration.IMPLEMENTATION,
        dependencyNotation = Dependencies.Logging.logbackClassic,
    ),
    Dependency.Explicit(
        configuration = Dependency.Configuration.IMPLEMENTATION,
        dependencyNotation = Dependencies.Logging.slf4j,
    ),
)

internal val DEFAULT_JVM_TEST_DEPENDENCIES: List<Dependency> = listOf(
    Dependency.Kotlin(
        configuration = Dependency.Configuration.IMPLEMENTATION,
        dependencyNotation = "test-junit5",
    ),
    Dependency.Explicit(
        configuration = Dependency.Configuration.RUNTIME_ONLY,
        dependencyNotation = Dependencies.JUnit.engine,
    ),
)

private fun DependencyHandlerScope.apply(dependency: Dependency) {
  val configurationName = dependency.configuration.configurationName
  val dependencyNotation: Any = when (dependency) {
    is Dependency.Explicit -> dependency.dependencyNotation
    is Dependency.Kotlin -> kotlin(dependency.dependencyNotation)
    is Dependency.Project -> project(dependency.dependencyNotation)
  }
  add(configurationName, dependencyNotation)
}
