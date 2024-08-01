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
}

private fun dependency(group: String, name: String, version: String): String =
  "$group:$name:$version"
