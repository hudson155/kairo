package limber.config.deserializer

import org.jetbrains.annotations.TestOnly

private typealias EnvironmentVariableGetter = (name: String) -> String?

private val defaultGetter: EnvironmentVariableGetter = System::getenv

/**
 * Fetches the value from an environment variable.
 */
internal object EnvironmentVariableSource {
  private var delegate: EnvironmentVariableGetter = defaultGetter

  operator fun get(name: String): String? =
    delegate(name)

  @TestOnly
  fun withOverride(get: (name: String) -> String?, block: () -> Unit) {
    delegate = get
    block()
    delegate = defaultGetter
  }
}