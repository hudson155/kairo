/**
 * Specifies plugins common to the entire project. Plugins with "apply false" simply define the plugin version without
 * actually applying it to the root project.
 */
plugins {
  kotlin("multiplatform") version Versions.kotlin apply false
  id(Plugins.detekt) version Versions.detekt apply false
  idea // This plugin allows the Gradle project to work seamlessly with IntelliJ IDEA.
}

allprojects {
  repositories {
    jcenter()
  }
}

/**
 * This task downloads dependencies, kinda like `yarn install`.
 */
tasks.create("downloadDependencies") {
  description = "Download all dependencies"
  doLast {
    configurations.forEach { if (it.isCanBeResolved) it.resolve() }
  }
}
