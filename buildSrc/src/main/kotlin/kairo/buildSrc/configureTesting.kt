package kairo.buildSrc

import org.gradle.api.Project
import org.gradle.api.tasks.testing.AbstractTestTask
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType

internal fun Project.configureTesting() {
  tasks.withType<AbstractTestTask> {
    testLogging {
      events("passed", "skipped", "failed")
    }
  }
  tasks.withType<Test> {
    useJUnitPlatform()
    ignoreFailures = project.hasProperty("ignoreTestFailures") // This property may be set during CI.
  }
  test.configure {
    dependencies {
      implementation(Dependencies.kotestRunner)
    }
  }
}
