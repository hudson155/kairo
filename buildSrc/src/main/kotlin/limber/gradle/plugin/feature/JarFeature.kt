package limber.gradle.plugin.feature

import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.withType

object JarFeature : PluginFeature {
  override fun configure(target: Project, context: PluginFeature.Context) {
    target.tasks.withType<Jar> {
      // Archives (JARs) are named using the fully qualified project path in order to avoid
      // collisions when multiple JARs are combined to form an application.
      archiveBaseName.set(project.path.drop(1).replace(':', '-'))
    }
  }
}
