plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  id(Plugins.detekt)
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        api(project(":limber-backend-application:common"))
        api(project(":piper:reps"))
        implementation(project(":piper:rest-interface"))
        implementation(project(":piper:serialization"))
        implementation(project(":piper:util"))
      }
    }
    jvm()
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
