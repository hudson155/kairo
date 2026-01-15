import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
  id("kairo")
  `java-library`
  kotlin("jvm")
  id("dev.detekt")
}

java {
  toolchain {
    languageVersion = javaVersion
  }
}

kotlin {
  jvmToolchain {
    languageVersion = javaVersion
  }
  explicitApi()
  compilerOptions {
    allWarningsAsErrors = true
    freeCompilerArgs.add("-Xannotation-default-target=param-property")
    freeCompilerArgs.add("-Xconsistent-data-class-copy-visibility")
    freeCompilerArgs.add("-Xjsr305=strict")
    freeCompilerArgs.add("-Xlambdas=indy")
    freeCompilerArgs.add("-opt-in=kotlin.concurrent.atomics.ExperimentalAtomicApi")
    freeCompilerArgs.add("-opt-in=kotlin.uuid.ExperimentalUuidApi")
  }
}

dependencies {
  implementation(platform(project(":bom-full")))
  detektPlugins("dev.detekt:detekt-rules-ktlint-wrapper:${detekt.toolVersion.get()}")
  testRuntimeOnly("org.slf4j:slf4j-simple")
}

/**
 * Detekt makes the [check] task depend on the [detekt] task automatically.
 * However, since the [detekt] task doesn't support type resolution,
 * some issues get missed.
 *
 * Here, we remove the default dependency and replace it with [detektMain] and [detektTest]
 * which do support type resolution.
 */
tasks.named("check").configure {
  setDependsOn(dependsOn.filterNot { it is TaskProvider<*> && it.name == "detekt" })
  dependsOn("detektMain", "detektTest")
}

tasks.test {
  jvmArgs(
    "-Dorg.slf4j.simpleLogger.defaultLogLevel=debug",
    "-Dorg.slf4j.simpleLogger.logFile=System.out",
  )
  testLogging {
    exceptionFormat = TestExceptionFormat.FULL
    events("passed", "skipped", "failed")
  }
  useJUnitPlatform()
}

detekt {
  config.from(files("$rootDir/.detekt/config.yaml"))
  parallel = true
  autoCorrect = System.getenv("CI") != "true"
}
