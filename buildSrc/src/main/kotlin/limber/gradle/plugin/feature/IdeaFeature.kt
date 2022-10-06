package limber.gradle.plugin.feature

import org.gradle.api.Project

object IdeaFeature : PluginFeature {
  override fun configure(target: Project, context: PluginFeature.Context) {
    target.pluginManager.apply("org.gradle.idea")
  }
}
