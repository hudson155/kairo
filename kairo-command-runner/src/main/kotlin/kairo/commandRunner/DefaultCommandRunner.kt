package kairo.commandRunner

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import java.io.BufferedReader

private val logger: KLogger = KotlinLogging.logger {}

public class DefaultCommandRunner : CommandRunner() {
  @Suppress("ForbiddenMethodCall")
  private val runtime: Runtime = Runtime.getRuntime()

  @Insecure
  override fun run(command: String): BufferedReader {
    logger.debug { "Running command (command=$command)." }
    val result = runtime.exec(arrayOf("bash", "-c", command))
    return result.inputReader()
  }
}
