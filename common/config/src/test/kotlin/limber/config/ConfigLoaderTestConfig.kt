package limber.config

internal data class ConfigLoaderTestConfig(
  override val clock: ClockConfig,
  override val ids: IdsConfig,
  override val name: String,
  override val server: ServerConfig,
) : Config()
