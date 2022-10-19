package limber.gradle.plugin.feature

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * [KotlinFeature] configures the Kotlin language and closely-related aspects of the build.
 */
class KotlinFeature(private val kotlinPluginId: String) : PluginFeature {
  override fun configure(target: Project, context: PluginFeature.Context) {
    target.pluginManager.apply(kotlinPluginId)
    target.repositories {
      mavenCentral()
    }
    target.extensions.configure<JavaPluginExtension> {
      toolchain {
        languageVersion.set(JavaLanguageVersion.of(JavaVersion.VERSION_17.toString()))
      }
    }
    target.extensions.configure<KotlinProjectExtension> {
      explicitApi()
    }
    target.tasks.withType<KotlinCompile> {
      kotlinOptions.allWarningsAsErrors = true
    }
  }
}
