import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

plugins {
  kotlin("multiplatform") version Versions.kotlin
  idea
  kotlin("plugin.serialization") version Versions.kotlin
  id(Plugins.detekt).version(Versions.detekt)
}

repositories {
  jcenter()
}

tasks.create("downloadDependencies") {
  description = "Download all dependencies"
  doLast {
    configurations.forEach { if (it.isCanBeResolved) it.resolve() }
  }
}

subprojects {
  buildscript {
    repositories {
      jcenter()
    }
  }

  repositories {
    jcenter()
    maven(url = "https://kotlin.bintray.com/kotlin-js-wrappers/")
  }

  tasks.withType<KotlinCompile<*>>().configureEach {
    (kotlinOptions as? KotlinJvmOptions)?.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlinx.serialization.ImplicitReflectionSerializer"
    kotlinOptions.freeCompilerArgs += "-Xallow-result-return-type"
  }
}
