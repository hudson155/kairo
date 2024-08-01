package kairo.buildSrc

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Configures Kairo publishing.
 * Modules that use this plugin will be published.
 */
public class KairoPublishPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      applyPublishPlugins()
      configurePublishing()
    }
  }
}
