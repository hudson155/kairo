import io.gitlab.arturbosch.detekt.DetektPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version Versions.kotlin
  id(Plugins.detekt) version Versions.detekt
}

subprojects {
  apply<KotlinPlatformJvmPlugin>()
  apply<DetektPlugin>()

  dependencies {
    implementation(kotlin("reflect"))
    if (path != ":limber-backend:common:util") {
      implementation(project(":limber-backend:common:util"))
    }
    if (!path.startsWith(":limber-backend:common:testing:")) {
      testImplementation(project(":limber-backend:common:testing:unit"))
    }
    implementation(Dependencies.Logging.slf4j)
  }

  tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.ExperimentalStdlibApi"
  }

  tasks.test {
    useJUnitPlatform()
    testLogging {
      events("passed", "skipped", "failed")
    }
  }

  detekt {
    config = files("$rootDir/.detekt/config.yaml")
    input = files("src/main/kotlin", "src/test/kotlin")
  }
}
