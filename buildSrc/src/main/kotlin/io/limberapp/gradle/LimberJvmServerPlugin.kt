package io.limberapp.gradle

import Plugins
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.Project

class LimberJvmServerPlugin : LimberJvmPlugin() {
  override val plugins: List<String>
    get() = super.plugins + Plugins.application + Plugins.shadow

  override fun configure(project: Project) {
    super.configure(project)
    setMetadata(project)
    configureShadowJarTask(project)
  }

  private fun setMetadata(project: Project) {
    project.group = "io.limberapp.server"
    project.version = "1-SNAPSHOT"
    project.application {
      // Shadow requires use of deprecated mainClassName.
      mainClassName = "io.ktor.server.cio.EngineMain"
    }
  }

  private fun configureShadowJarTask(project: Project) {
    project.tasks.withType(ShadowJar::class.java) {
      archiveFileName.set("limber-server.jar")
      mergeServiceFiles()
      manifest {
        attributes(mapOf("MainClass" to project.application.mainClassName))
      }
    }
  }
}
