package kairo.buildSrc

import org.gradle.api.Project

internal fun Project.addLoggingDependencies() {
  main.configure {
    dependencies {
      implementation(project(":kairo-logging"))
    }
  }
  test.configure {
    dependencies {
      implementation(project(":kairo-logging:testing"))
    }
  }
}
