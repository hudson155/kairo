package limber.gradle.plugin.feature

import org.gradle.api.Project

/**
 * [IdeaFeature] makes it easy to open the project using IntelliJ IDEA.
 */
object IdeaFeature : PluginFeature {
  override fun configure(target: Project, context: PluginFeature.Context) {
    target.pluginManager.apply("org.gradle.idea")
  }
}
