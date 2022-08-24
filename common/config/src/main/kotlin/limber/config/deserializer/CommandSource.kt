package limber.config.deserializer

import org.jetbrains.annotations.TestOnly

private typealias CommandGetter = (command: String) -> String

private val defaultGetter: CommandGetter = { command ->
  Runtime.getRuntime().exec(arrayOf("sh", "-c", command)).inputReader().readLines().single()
}

/**
 * Runs a shell command to get the value.
 */
internal object CommandSource {
  private var delegate: CommandGetter = defaultGetter

  operator fun get(name: String): String =
    delegate(name)

  @TestOnly
  fun withOverride(get: (name: String) -> String, block: () -> Unit) {
    delegate = get
    block()
    delegate = defaultGetter
  }
}
