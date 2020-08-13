plugins {
  kotlin("multiplatform")
  id(Plugins.detekt)
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(project(":piper:types"))
        implementation(project(":piper:validation"))
      }
    }
    jvm {
      compilations["main"].defaultSourceSet {
        dependencies {
          implementation(project(":piper:util"))
        }
      }
    }
    js {
      browser()
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
