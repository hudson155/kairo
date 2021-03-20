import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

plugins {
  id("limber-jvm-library")
}

tasks.withType<KotlinJvmCompile>().configureEach {
  kotlinOptions.freeCompilerArgs += "-Xallow-kotlin-package"
}
