plugins {
  java
  kotlin("jvm")
  id("com.google.cloud.artifactregistry.gradle-plugin")
  id("io.gitlab.arturbosch.detekt")
}

repositories {
  mavenLocal()
  mavenCentral()
  maven {
    url = uri("https://jitpack.io")
  }
  maven {
    url = uri("artifactregistry://us-central1-maven.pkg.dev/airborne-software/maven")
  }
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

kotlin {
  jvmToolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
  explicitApi()
  compilerOptions {
    allWarningsAsErrors = true
    freeCompilerArgs.add("-Xconsistent-data-class-copy-visibility")
    freeCompilerArgs.add("-opt-in=kotlin.uuid.ExperimentalUuidApi")
  }
}

dependencies {
  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:${detekt.toolVersion}")
}

/**
 * Detekt makes the [check] task depend on the [detekt] task automatically.
 * However, since the [detekt] task doesn't support type resolution
 * (at least, not until the next major version of Detekt),
 * some issues get missed.
 *
 * Here, we remove the default dependency and replace it with [detektMain] and [detektTest]
 * which do support type resolution.
 *
 * This can be removed once the next major version of Detekt is released.
 */
tasks.named("check").configure {
  setDependsOn(dependsOn.filterNot { it is TaskProvider<*> && it.name == "detekt" })
  dependsOn("detektMain", "detektTest")
}

tasks.test {
  testLogging {
    events("passed", "skipped", "failed")
  }
  useJUnitPlatform()
  ignoreFailures = project.hasProperty("ignoreTestFailures") // This property may be set during CI.
}

detekt {
  config.from(files("$rootDir/.detekt/config.yaml"))
  parallel = true
}
