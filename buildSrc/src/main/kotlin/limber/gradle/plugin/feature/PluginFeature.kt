package limber.gradle.plugin.feature

import limber.gradle.plugin.SourceSet
import org.gradle.api.Project

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
    fun commonMain(target: Project): SourceSet
    fun commonTest(target: Project): SourceSet
    fun jvmMain(target: Project): SourceSet
    fun jvmTest(target: Project): SourceSet
  }

  fun configure(target: Project, context: Context)
}
