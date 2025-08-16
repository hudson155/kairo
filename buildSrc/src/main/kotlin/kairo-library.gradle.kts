plugins {
  id("kairo")
  `java-library`
  kotlin("jvm")
  id("io.gitlab.arturbosch.detekt")
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
    freeCompilerArgs.add("-Xcontext-parameters")
    freeCompilerArgs.add("-Xjsr305=strict")
    freeCompilerArgs.add("-Xlambdas=indy")
    freeCompilerArgs.add("-Xnested-type-aliases")
    freeCompilerArgs.add("-opt-in=kotlin.concurrent.atomics.ExperimentalAtomicApi")
    freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
    freeCompilerArgs.add("-opt-in=kotlin.uuid.ExperimentalUuidApi")
  }
}

dependencies {
  api(platform(project(":bom-full")))
  testRuntimeOnly("org.slf4j:slf4j-simple")
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
  jvmArgs(
    "-Dorg.slf4j.simpleLogger.defaultLogLevel=debug",
    "-Dorg.slf4j.simpleLogger.logFile=System.out",
  )
  testLogging {
    events("passed", "skipped", "failed")
  }
  useJUnitPlatform()
}

detekt {
  config.from(files("$rootDir/.detekt/config.yaml"))
  parallel = true
  autoCorrect = true
}
