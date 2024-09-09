package kairo.config

import kairo.environmentVariableSupplier.DefaultEnvironmentVariableSupplier
import kairo.environmentVariableSupplier.EnvironmentVariableSupplier

public data class ConfigLoaderConfig(
  val environmentVariableSupplier: EnvironmentVariableSupplier,
) {
  public constructor() : this(
    environmentVariableSupplier = DefaultEnvironmentVariableSupplier,
  )
}
