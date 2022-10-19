package limber.gradle.plugin.feature

import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

/**
 * Plugins are separated into [PluginFeature]s
 * in order to reduce the length of any particular plugin,
 * and to allow code sharing between plugins.
 */
interface PluginFeature {
  /**
   * Provides an interface for installing both multiplatform and JVM-specific dependencies.
   */
  interface Context {
    abstract fun commonMain(target: Project): NamedDomainObjectProvider<KotlinSourceSet>
    abstract fun commonTest(target: Project): NamedDomainObjectProvider<KotlinSourceSet>
    abstract fun jvmMain(target: Project): NamedDomainObjectProvider<KotlinSourceSet>
    abstract fun jvmTest(target: Project): NamedDomainObjectProvider<KotlinSourceSet>
  }

  abstract fun configure(target: Project, context: Context)
}
