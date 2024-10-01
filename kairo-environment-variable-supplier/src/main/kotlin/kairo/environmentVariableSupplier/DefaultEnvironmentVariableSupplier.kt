package kairo.environmentVariableSupplier

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger: KLogger = KotlinLogging.logger {}

public object DefaultEnvironmentVariableSupplier : EnvironmentVariableSupplier() {
  public override operator fun get(name: String, default: String?): String? {
    logger.debug { "Getting environment variable: $name." }
    @Suppress("ForbiddenMethodCall")
    return System.getenv(name) ?: default
  }
}
