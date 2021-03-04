import io.gitlab.arturbosch.detekt.DetektPlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

/**
 * Specifies plugins common to the entire project. Plugins with "apply false" simply define the
 * plugin version without actually applying it to the root project.
 */
plugins {
  kotlin("multiplatform") version Versions.kotlin apply false
  id(Plugins.detekt) version Versions.detekt
  idea // This plugin allows the Gradle project to work seamlessly with IntelliJ IDEA.
}

allprojects {
  apply<DetektPlugin>()

  repositories {
    mavenCentral()
    // https://jfrog.com/blog/into-the-sunset-bintray-jcenter-gocenter-and-chartcenter/.
    jcenter() // JCenter is EOL.
  }

  tasks.withType<KotlinJsCompile> {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.js.ExperimentalJsExport"
  }

  tasks.withType<KotlinJvmCompile> {
    kotlinOptions.jvmTarget = "11"
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

  detekt {
    config = files("$rootDir/.detekt/config.yaml")
    input = files(file("src").listFiles()?.map { "src/${it.name}/kotlin" })
  }
}
