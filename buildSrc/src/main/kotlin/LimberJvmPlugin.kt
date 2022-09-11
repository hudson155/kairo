import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.testing.AbstractTestTask
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.tasks.Jar
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.add
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.exclude
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.withType
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

/**
 * Configures JVM Gradle modules.
 * Unless and until Multiplatform modules are introduced,
 * this should be used in all Gradle modules.
 */
class LimberJvmPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    configureIdea(target)
    configureJvm(target)
    configureTesting(target)
    configureDetekt(target)
    installProjectDependencies(target)
    configureJar(target)
  }

  private fun configureIdea(target: Project) {
    target.pluginManager.apply("org.gradle.idea")
  }

  private fun configureJvm(target: Project) {
    target.pluginManager.apply("org.jetbrains.kotlin.jvm")
    target.repositories {
      mavenCentral()
    }
    target.extensions.configure<JavaPluginExtension> {
      toolchain {
        languageVersion.set(JavaLanguageVersion.of(JavaVersion.VERSION_17.toString()))
      }
    }
    target.extensions.configure<KotlinJvmProjectExtension> {
      explicitApi()
    }
    target.tasks.withType<KotlinJvmCompile> {
      kotlinOptions.allWarningsAsErrors = true
    }
    target.dependencies {
      add("implementation", kotlin("reflect"))
    }
  }

  private fun configureTesting(target: Project) {
    target.tasks.withType<AbstractTestTask> {
      testLogging {
        events("passed", "skipped", "failed")
      }
    }
    target.tasks.withType<Test> {
      useJUnitPlatform()
      ignoreFailures = project.hasProperty("ignoreTestFailures")
    }
    target.dependencies {
      add("testImplementation", Dependencies.Testing.Junit.api)
      add("testRuntimeOnly", Dependencies.Testing.Junit.engine)
      add("testImplementation", Dependencies.Testing.Kotest.assertions)
    }
  }

  private fun configureDetekt(target: Project) {
    target.pluginManager.apply("io.gitlab.arturbosch.detekt")
    target.dependencies {
      add("detektPlugins", Dependencies.Detekt.formatting) {
        exclude("org.slf4j")
      }
    }
    target.extensions.configure<DetektExtension> {
      toolVersion = Versions.detekt
      config = target.files("${target.rootDir}/.detekt/config.yml")
      parallel = true
      buildUponDefaultConfig = true
    }
    /**
     * Detekt makes the "check" task depend on the "detekt" task automatically.
     * However, since the "detekt" task doesn't support type resolution
     * (at least, not until the next major version of Detekt),
     * some issues get missed.
     *
     * Here, we remove the default dependency and replace it with "detektMain" and "detektTest"
     * which do support type resolution.
     *
     * This can be removed once the next major version of Detekt is released.
     */
    target.tasks.matching { it.name == LifecycleBasePlugin.CHECK_TASK_NAME }.configureEach {
      setDependsOn(dependsOn.filter { it !is TaskProvider<*> || it.name != DetektPlugin.DETEKT_TASK_NAME })
      dependsOn("detektMain", "detektTest")
    }
  }

  /**
   * Installs the list of dependencies in all Gradle modules.
   * To avoid recursive dependency issues,
   * modules listed here will only have those dependencies listed prior,
   * but all modules not listed here will have the entire list.
   */
  private fun installProjectDependencies(target: Project) {
    target.dependencies {
      val paths = mutableSetOf<String>()
      listOf(":common:logging", ":common:util").map { path ->
        paths += path
        if (target.path !in paths) add("implementation", project(path))
      }
    }
  }

  private fun configureJar(target: Project) {
    target.tasks.withType<Jar> {
      // Archives (JARs) are named using the fully qualified project path in order to avoid
      // collisions when multiple JARs are combined to form an application.
      archiveBaseName.set(project.path.drop(1).replace(':', '-'))
    }
  }
}
