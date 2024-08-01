package kairo.buildSrc

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlin() {
  pluginManager.apply("org.jetbrains.kotlin.jvm")
  repositories {
    mavenCentral()
  }
  extensions.configure<JavaPluginExtension> {
    toolchain {
      languageVersion.set(JavaLanguageVersion.of(JavaVersion.VERSION_21.toString()))
    }
  }
  extensions.configure<KotlinJvmProjectExtension> {
    explicitApi()
    compilerOptions {
      allWarningsAsErrors.set(true)
    }
  }
}
