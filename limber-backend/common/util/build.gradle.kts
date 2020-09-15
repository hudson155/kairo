import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
  kotlin("multiplatform")
  id(Plugins.detekt)
}

kotlin {
  sourceSets {
    commonTest {
      dependencies {
        implementation(kotlin("test-annotations-common"))
        implementation(kotlin("test-common"))
      }
    }
    jvm {
      compilations["main"].defaultSourceSet {
        dependencies {
          implementation(kotlin("reflect"))
        }
      }
      compilations["test"].defaultSourceSet {
        dependencies {
          implementation(kotlin("test-junit5"))
          runtimeOnly(Dependencies.JUnit.engine)
        }
      }
    }
    js {
      browser()
      compilations["main"].defaultSourceSet {
        dependencies {
          implementation(Dependencies.Kotlin.extensions)
        }
      }
      compilations["test"].defaultSourceSet {
        dependencies {
          implementation(kotlin("test-js"))
        }
      }
    }
  }
}

tasks.withType<KotlinCompile<*>>().configureEach {
  kotlinOptions.freeCompilerArgs += "-Xallow-kotlin-package"
}

tasks.named<Test>("jvmTest") {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files(
    "src/commonMain/kotlin",
    "src/commonTest/kotlin",
    "src/jsMain/kotlin",
    "src/jsTest/kotlin",
    "src/jvmMain/kotlin",
    "src/jvmTest/kotlin"
  )
}
