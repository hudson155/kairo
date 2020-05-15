plugins {
  kotlin("multiplatform")
  id(Plugins.detekt)
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(kotlin("stdlib-common"))
        implementation(project(":piper:data-conversion"))
        implementation(project(":piper:types"))
        api(Dependencies.Serialization.common)
      }
    }
    jvm {
      compilations["main"].defaultSourceSet {
        dependencies {
          implementation(kotlin("stdlib-jdk8"))
          api(Dependencies.Serialization.jvm)
        }
      }
    }
    js {
      browser { }
      compilations["main"].defaultSourceSet {
        dependencies {
          implementation(kotlin("stdlib-js"))
          api(Dependencies.Serialization.js)
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
