package kairo.environmentVariableSupplier

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger: KLogger = KotlinLogging.logger {}

@Suppress("NotImplementedDeclaration")
public class StaticEnvironmentVariableSupplier(
  private val environmentVariables: Map<String, String?>,
) : EnvironmentVariableSupplier() {
  /**
   * Note: This implementation ignores [default].
   */
  public override fun get(name: String, default: String?): String? {
    logger.debug { "Getting environment variable: $name." }
    if (name !in environmentVariables) throw NotImplementedError("Environment variable not provided: $name.")
    return environmentVariables[name]
  }
}
