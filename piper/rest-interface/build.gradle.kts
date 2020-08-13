plugins {
  kotlin("multiplatform")
  id(Plugins.detekt)
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(project(":piper:reps"))
        implementation(project(":piper:util"))
      }
    }
    jvm {
      compilations["main"].defaultSourceSet {
        dependencies {
          implementation(kotlin("reflect"))
          implementation(Dependencies.Logging.slf4j)
        }
      }
    }
    js {
      browser()
      compilations["main"].defaultSourceSet {
        dependencies {
          implementation(project(":piper:serialization"))
          implementation(Dependencies.Kotlin.extensions)
          implementation(Dependencies.Kotlinx.coroutinesJs)
        }
      }
    }
  }
}

detekt {
  config = files("$rootDir/.detekt/config.yml")
  input = files(
    "src/commonMain/kotlin",
    "src/commonTest/kotlin",
    "src/jsMain/kotlin",
    "src/jsTest/kotlin",
    "src/jvmMain/kotlin",
    "src/jvmTest/kotlin"
  )
}
