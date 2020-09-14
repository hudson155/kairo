plugins {
  kotlin("multiplatform")
  id(Plugins.detekt)
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(project(":limber-backend:common:data-conversion"))
        implementation(project(":limber-backend:common:types"))
        api(Dependencies.Serialization.common)
      }
    }
    jvm {
      compilations["main"].defaultSourceSet {
        dependencies {
          api(Dependencies.Serialization.jvm)
        }
      }
    }
    js {
      browser()
      compilations["main"].defaultSourceSet {
        dependencies {
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
