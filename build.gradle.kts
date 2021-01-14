import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

/**
 * Specifies plugins common to the entire project. Plugins with "apply false" simply define the plugin version without
 * actually applying it to the root project.
 */
plugins {
  kotlin("multiplatform") version Versions.kotlin apply false
  idea // This plugin allows the Gradle project to work seamlessly with IntelliJ IDEA.
}

allprojects {
  repositories {
    jcenter()
  }

  tasks.withType<KotlinJsCompile> {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.js.ExperimentalJsExport"
  }

  tasks.withType<KotlinJvmCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.ExperimentalStdlibApi"
  }

  tasks.withType<AbstractTestTask> {
    testLogging {
      events("passed", "skipped", "failed")
    }
  }

  tasks.withType<Test> {
    useJUnitPlatform()
  }
}
