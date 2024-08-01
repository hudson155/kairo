package kairo.buildSrc

import org.gradle.api.Plugin
import org.gradle.api.Project

public class KairoPublishPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      applyPublishPlugins()
      configurePublishing()
    }
  }
}
