package kairo.config

import kairo.commandRunner.CommandRunner
import kairo.commandRunner.DefaultCommandRunner
import kairo.environmentVariableSupplier.DefaultEnvironmentVariableSupplier
import kairo.environmentVariableSupplier.EnvironmentVariableSupplier
import kairo.gcpSecretSupplier.DefaultGcpSecretSupplier
import kairo.gcpSecretSupplier.GcpSecretSupplier

public data class ConfigLoaderConfig(
  val commandRunner: CommandRunner,
  val environmentVariableSupplier: EnvironmentVariableSupplier,
  val gcpSecretSupplier: GcpSecretSupplier,
) {
  public constructor() : this(
    commandRunner = DefaultCommandRunner,
    environmentVariableSupplier = DefaultEnvironmentVariableSupplier,
    gcpSecretSupplier = DefaultGcpSecretSupplier,
  )
}
