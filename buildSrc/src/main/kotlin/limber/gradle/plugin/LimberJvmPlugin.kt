package limber.gradle.plugin

import limber.gradle.main
import limber.gradle.plugin.feature.DependenciesFeature
import limber.gradle.plugin.feature.DetektFeature
import limber.gradle.plugin.feature.IdeaFeature
import limber.gradle.plugin.feature.JarFeature
import limber.gradle.plugin.feature.KotlinFeature
import limber.gradle.plugin.feature.ProjectDependenciesFeature
import limber.gradle.plugin.feature.TestingFeature
import limber.gradle.test
import org.gradle.api.Project

/**
 * Configures JVM Gradle modules.
 * Unless and until Multiplatform modules are introduced,
 * this should be used in all Gradle modules.
 */
class LimberJvmPlugin : LimberBasePlugin() {
  override fun commonMain(target: Project) = target.main
  override fun commonTest(target: Project) = target.test
  override fun jvmMain(target: Project) = target.main
  override fun jvmTest(target: Project) = target.test

  override val features = listOf(
    IdeaFeature,
    KotlinFeature("org.jetbrains.kotlin.jvm"),
    DependenciesFeature,
    TestingFeature,
    DetektFeature,
    ProjectDependenciesFeature,
    JarFeature,
  )
}
