plugins {
  kotlin("multiplatform")
  id(Plugins.detekt)
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(project(":limber-backend:common:util"))
      }
    }
    jvm()
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
