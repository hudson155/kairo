package kairo.buildSrc

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Configures shared settings for all Kairo modules.
 */
public class KairoPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      applyIdeaPlugin()
      configureKotlin()
      configureTesting()
    }
  }
}
