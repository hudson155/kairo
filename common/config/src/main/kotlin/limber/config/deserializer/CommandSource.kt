package limber.config.deserializer

private typealias CommandGetter = (command: String) -> String

private val defaultGetter: CommandGetter = { command ->
  val runtime = Runtime.getRuntime()
  val result = runtime.exec(arrayOf("sh", "-c", command))
  result.inputReader().readLines().single()
}

/**
 * Runs a shell command to get the value.
 */
internal object CommandSource {
  private val delegate: CommandGetter = defaultGetter

  operator fun get(name: String): String = delegate(name)
}
