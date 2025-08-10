package kairo.environmentVariableSupplier

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger: KLogger = KotlinLogging.logger {}

public object DefaultEnvironmentVariableSupplier : EnvironmentVariableSupplier() {
  override fun get(name: String): String? {
    logger.debug { "Getting environment variable: $name." }
    @Suppress("ForbiddenMethodCall")
    return System.getenv(name)
  }
}
