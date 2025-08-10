package kairo.commandRunner

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger: KLogger = KotlinLogging.logger {}

public object DefaultCommandRunner : CommandRunner() {
  @Insecure
  override fun run(command: String): Sequence<String> {
    logger.debug { "Running command: $command." }
    @Suppress("ForbiddenMethodCall")
    val runtime = Runtime.getRuntime()
    val result = runtime.exec(arrayOf("bash", "-c", command))
    return result.inputReader().lineSequence()
  }
}
