package limber.gradle.plugin.feature

import limber.gradle.Dependencies
import org.gradle.api.Project

/**
 * Installs default dependencies.
 * These will apply for every module that uses the plugin.
 */
object DependenciesFeature : PluginFeature {
  override fun configure(target: Project, context: PluginFeature.Context) {
    context.jvmMain(target).configure {
      dependencies {
        implementation(kotlin("reflect"))
        implementation(Dependencies.Kotlinx.coroutines)
      }
    }
  }
}
