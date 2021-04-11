package io.limberapp.gradle

sealed class Dependency {
  enum class Configuration(val configurationName: String) {
    IMPLEMENTATION("implementation"),
    RUNTIME_ONLY("runtimeOnly"),
    TEST_IMPLEMENTATION("testImplementation"),
    TEST_RUNTIME_ONLY("testRuntimeOnly");

    fun test(): Configuration =
        when (this) {
          IMPLEMENTATION -> TEST_IMPLEMENTATION
          RUNTIME_ONLY -> TEST_RUNTIME_ONLY
          TEST_IMPLEMENTATION, TEST_RUNTIME_ONLY -> error("Configuration is already test.")
        }
  }

  abstract val configuration: Configuration

  abstract fun test(): Dependency

  data class Explicit(
      override val configuration: Configuration,
      val dependencyNotation: String,
  ) : Dependency() {
    override fun test(): Dependency = copy(configuration = configuration.test())
  }

  data class Kotlin(
      override val configuration: Configuration,
      val dependencyNotation: String,
  ) : Dependency() {
    override fun test(): Dependency = copy(configuration = configuration.test())
  }

  data class Project(
      override val configuration: Configuration,
      val dependencyNotation: String,
  ) : Dependency() {
    override fun test(): Dependency = copy(configuration = configuration.test())
  }
}

fun List<Dependency>.test(): List<Dependency> = map { it.test() }
