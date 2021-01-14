kotlin {
  sourceSets {
    jvmMain {
      dependencies {
        api(Dependencies.Logging.slf4j)
      }
    }
  }
}
