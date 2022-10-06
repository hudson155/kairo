package limber.gradle.plugin

import limber.gradle.plugin.feature.PluginFeature
import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class LimberBasePlugin constructor() : Plugin<Project>, PluginFeature.Context {
  abstract val features: List<PluginFeature>

  final override fun apply(target: Project) {
    features.forEach { it.configure(target, this) }
  }
}
