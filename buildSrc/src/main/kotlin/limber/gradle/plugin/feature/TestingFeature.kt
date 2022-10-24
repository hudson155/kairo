package limber.gradle.plugin.feature

import limber.gradle.Dependencies
import org.gradle.api.Project
import org.gradle.api.tasks.testing.AbstractTestTask
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType

/**
 * Configures testing and installs the necessary dependencies.
 */
object TestingFeature : PluginFeature {
  override fun configure(target: Project, context: PluginFeature.Context) {
    target.tasks.withType<AbstractTestTask> {
      testLogging {
        events("passed", "skipped", "failed")
      }
    }
    target.tasks.withType<Test> {
      useJUnitPlatform()
      ignoreFailures = project.hasProperty("ignoreTestFailures") // This property may be set during CI.
    }
    context.commonTest(target).configure {
      dependencies {
        implementation(Dependencies.Testing.Kotest.assertions)
      }
    }
    context.jvmTest(target).configure {
      dependencies {
        implementation(Dependencies.Testing.Junit.api)
        runtimeOnly(Dependencies.Testing.Junit.engine)
      }
    }
  }
}
