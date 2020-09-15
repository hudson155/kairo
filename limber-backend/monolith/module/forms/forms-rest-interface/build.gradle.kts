plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  id(Plugins.detekt)
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        api(project(":limber-backend:common:reps"))
        implementation(project(":limber-backend:common:rest-interface"))
        implementation(project(":limber-backend:common:serialization"))
        implementation(project(":limber-backend:common:util"))
        api(project(":limber-backend:monolith:common"))
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
