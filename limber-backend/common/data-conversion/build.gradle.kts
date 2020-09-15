plugins {
  kotlin("multiplatform")
  id(Plugins.detekt)
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(project(":limber-backend:common:types"))
        implementation(project(":limber-backend:common:validation"))
      }
    }
    jvm {
      compilations["main"].defaultSourceSet {
        dependencies {
          implementation(project(":limber-backend:common:util"))
        }
      }
    }
    js {
      browser()
    }
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
