package kairo.buildSrc

import org.gradle.api.Project

internal fun Project.applyIdeaPlugin() {
  pluginManager.apply("org.gradle.idea")
}
