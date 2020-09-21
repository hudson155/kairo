import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  implementation(kotlin("reflect"))

  implementation(Dependencies.Logging.slf4j)

  testImplementation(kotlin("test-junit5"))

  testRuntimeOnly(Dependencies.JUnit.engine)
}

tasks.withType<KotlinCompile<*>>().configureEach {
  kotlinOptions.freeCompilerArgs += "-Xallow-kotlin-package"
}

tasks.test {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
