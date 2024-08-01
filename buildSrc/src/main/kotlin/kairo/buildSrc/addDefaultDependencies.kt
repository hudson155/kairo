package kairo.buildSrc

import org.gradle.api.Project

internal fun Project.addDefaultDependencies() {
  main.configure {
    dependencies {
      implementation(kotlin("reflect"))
      implementation(Dependencies.kotlinxCoroutines)
    }
  }
}
