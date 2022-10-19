package limber.gradle.plugin.feature

import org.gradle.api.Project

/**
 * [ProjectDependenciesFeature] installs a list of project dependencies in all Gradle modules.
 * To avoid recursive dependency issues,
 * modules listed here will only have those dependencies listed prior,
 * but all modules not listed here will have the entire list.
 */
object ProjectDependenciesFeature : PluginFeature {
  override fun configure(target: Project, context: PluginFeature.Context) {
    context.jvmMain(target).configure {
      dependencies {
        val paths = mutableSetOf<String>()
        listOf(":common:logging", ":common:util").map { path ->
          paths += path
          if (target.path !in paths) implementation(project(path))
        }
      }
    }
  }
}
