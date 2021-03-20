package io.limberapp.gradle.task

import io.limberapp.gradle.kotlin
import org.gradle.api.Project
import org.gradle.api.Task

abstract class GradleTask {
  abstract val name: String

  open val dependencies: List<String>? = null

  open val isDependencyOf: List<String>? = null

  fun applyTo(project: Project) {
    project.kotlin {
      project.task(name) {
        dependencies?.let { taskNames -> dependsOn(*taskNames.toTypedArray()) }
        run(project)
        isDependencyOf?.let {
          it.forEach { taskName ->
            project.tasks.named(taskName).get().dependsOn(name)
          }
        }
      }
    }
  }

  protected abstract fun Task.run(project: Project)
}
