package limber.gradle.plugin.feature

import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.withType

/**
 * By default, JAR archives are named according to the Gradle module name.
 * This causes collisions when multiple Gradle modules have the same name,
 * which can happen with nested multimodule projects.
 *
 * This plugin changes the archive name to be the fully-qualified project path instead,
 * which helps avoid these colisions.
 */
object JarFeature : PluginFeature {
  override fun configure(target: Project, context: PluginFeature.Context) {
    target.tasks.withType<Jar> {
      archiveBaseName.set(project.path.drop(1).replace(':', '-'))
    }
  }
}
