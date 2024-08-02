package kairo.buildSrc

public object Dependencies {
  public val guice: String =
    dependency("com.google.inject", "guice", Versions.guice)

  public val kotestRunner: String =
    dependency("io.kotest", "kotest-runner-junit5", Versions.kotest)

  public val kotlinLoggingJvm: String =
    dependency("io.github.oshai", "kotlin-logging-jvm", Versions.kotlinLogging)

  public val kotlinxCoroutines: String =
    dependency("org.jetbrains.kotlinx", "kotlinx-coroutines-core", Versions.kotlinxCoroutines)

  public val kotlinxCoroutinesSlf4j: String =
    dependency("org.jetbrains.kotlinx", "kotlinx-coroutines-slf4j", Versions.kotlinxCoroutines)

  public val log4jCore: String =
    dependency("org.apache.logging.log4j", "log4j-core", Versions.log4j)

  public val log4jSlf4j2Impl: String =
    dependency("org.apache.logging.log4j", "log4j-slf4j2-impl", Versions.log4j)

  public val log4jConfigYaml: String =
    dependency("org.apache.logging.log4j", "log4j-config-yaml", Versions.log4j)
}

private fun dependency(group: String, name: String, version: String): String =
  "$group:$name:$version"
