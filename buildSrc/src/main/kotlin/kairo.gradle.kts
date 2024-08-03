plugins {
  kotlin("jvm")
}

repositories {
  mavenCentral()
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
  }
}

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
  }
  explicitApi()
  compilerOptions {
    allWarningsAsErrors.set(true)
  }
}

tasks.test {
  testLogging {
    events("passed", "skipped", "failed")
  }
  useJUnitPlatform()
  ignoreFailures = project.hasProperty("ignoreTestFailures") // This property may be set during CI.
}
