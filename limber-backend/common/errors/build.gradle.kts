plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  id(Plugins.detekt)
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(Dependencies.Serialization.common)
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
