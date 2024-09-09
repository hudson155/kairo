package kairo.config

import kairo.commandRunner.CommandRunner
import kairo.environmentVariableSupplier.EnvironmentVariableSupplier
import kairo.gcpSecretSupplier.GcpSecretSupplier

public data class ConfigLoaderConfig(
  val commandRunner: CommandRunner,
  val environmentVariableSupplier: EnvironmentVariableSupplier,
  val gcpSecretSupplier: GcpSecretSupplier,
)
