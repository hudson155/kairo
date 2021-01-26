kotlin {
  sourceSets {
    jvmMain {
      dependencies {
        api(Dependencies.Logging.logbackClassic)
        api(Dependencies.Logging.slf4j)
      }
    }
  }
}
