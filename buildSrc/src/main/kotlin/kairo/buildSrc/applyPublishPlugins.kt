package kairo.buildSrc

import org.gradle.api.Project

internal fun Project.applyPublishPlugins() {
  pluginManager.apply("maven-publish")
  pluginManager.apply("com.google.cloud.artifactregistry.gradle-plugin")
}
