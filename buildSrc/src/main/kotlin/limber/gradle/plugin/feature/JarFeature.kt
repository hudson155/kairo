package limber.gradle.plugin.feature

import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.withType

/**
 * By default, JAR archives are named according to the Gradle module name.
 * This causes colisions when multiple Gradle modules have the same name,
 * which can happen with nested multi-module projects.
 * [JarFeature] changes the archive name to be the fully-qualified project path instead,
 * which helps avoid these colisions.
 */
object JarFeature : PluginFeature {
  override fun configure(target: Project, context: PluginFeature.Context) {
    target.tasks.withType<Jar> {
      archiveBaseName.set(project.path.drop(1).replace(':', '-'))
    }
  }
}
