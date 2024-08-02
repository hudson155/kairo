package kairo.buildSrc

import org.gradle.api.Project
import org.gradle.api.tasks.testing.AbstractTestTask
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.base.TestingExtension

internal fun Project.configureTesting() {
  tasks.withType<Test> {
    testLogging {
      events("passed", "skipped", "failed")
    }
    useJUnitPlatform()
    ignoreFailures = project.hasProperty("ignoreTestFailures") // This property may be set during CI.
  }
  test.configure {
    dependencies {
      implementation(Dependencies.kotestRunner)
      implementation(Dependencies.mockk)
    }
  }
}
