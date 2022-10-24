package limber.gradle.plugin.feature

import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import limber.gradle.Dependencies
import limber.gradle.Versions
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.add
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.exclude
import org.gradle.language.base.plugins.LifecycleBasePlugin

/**
 * Installs and configures Detekt.
 * Detekt will run as part of Gradle's "check" task.
 */
object DetektFeature : PluginFeature {
  override fun configure(target: Project, context: PluginFeature.Context) {
    target.pluginManager.apply("io.gitlab.arturbosch.detekt")

    target.dependencies {
      add("detektPlugins", Dependencies.Detekt.formatting) {
        exclude("org.slf4j")
      }
    }

    target.extensions.configure<DetektExtension> {
      toolVersion = Versions.detekt
      config = target.files("${target.rootDir}/.detekt/config.yml")
      parallel = true
      buildUponDefaultConfig = true
    }

    /**
     * Detekt makes the "check" task depend on the "detekt" task automatically.
     * However, since the "detekt" task doesn't support type resolution
     * (at least, not until the next major version of Detekt),
     * some issues get missed.
     *
     * Here, we remove the default dependency and replace it with "detektMain" and "detektTest"
     * which do support type resolution.
     *
     * This can be removed once the next major version of Detekt is released.
     */
    target.tasks.matching { it.name == LifecycleBasePlugin.CHECK_TASK_NAME }.configureEach {
      setDependsOn(dependsOn.filter { it !is TaskProvider<*> || it.name != DetektPlugin.DETEKT_TASK_NAME })
      dependsOn("detektMain", "detektTest")
    }
  }
}
