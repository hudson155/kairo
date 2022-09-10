import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaApplication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

/**
 * This plugin should be applied to Server implementations.
 * It configures the JVM application,
 * and uses the [ShadowJar] plugin to create a shaded (fat) JAR file
 * so that the application can be run from a single file.
 */
class LimberJvmServerPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    configureApplication(target)
    configureShadowJar(target)
  }

  private fun configureApplication(target: Project) {
    target.pluginManager.apply("org.gradle.application")
    target.group = "limber.server"
    target.version = "1-SNAPSHOT" // This needs to be set, but is never used.
    target.extensions.configure<JavaApplication> {
      mainClass.set("limber.server.MainKt") // The main file must be this.
    }
  }

  private fun configureShadowJar(target: Project) {
    target.pluginManager.apply("com.github.johnrengelman.shadow")
    target.tasks.withType<ShadowJar> {
      archiveFileName.set("server.jar")
      mergeServiceFiles()
      manifest {
        attributes(mapOf("MainClass" to target.extensions.getByType<JavaApplication>().mainClass.get()))
      }
      // Something weird happens when we try to create a shaded JAR,
      // resulting in a failure to read the log4j2.xml config due to a classpath conflict.
      // This fixes it. See https://stackoverflow.com/a/61475766 for more details.
      transform(Log4j2PluginsCacheFileTransformer::class.java)
    }
  }
}
