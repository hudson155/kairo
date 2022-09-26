package limber.config

internal data class ConfigLoaderTestConfig(
  override val clock: ClockConfig,
  override val guids: GuidsConfig,
  override val name: String,
  override val server: ServerConfig,
) : Config()
