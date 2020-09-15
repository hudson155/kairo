plugins {
  kotlin("multiplatform")
  id(Plugins.detekt)
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        api(project(":limber-backend:common:types"))
        api(project(":limber-backend:common:validation"))
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
