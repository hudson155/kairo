import limber.gradle.Dependencies
import limber.gradle.plugin.main
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
  id("limber-jvm")
}

tasks.withType<KotlinJvmCompile> {
  // Some utilities are in the Kotlin package, so they don't need to be imported explicitly.
  kotlinOptions.freeCompilerArgs += "-Xallow-kotlin-package"
}

main {
  dependencies {
    api(Dependencies.Google.apiCommon)
  }
}
