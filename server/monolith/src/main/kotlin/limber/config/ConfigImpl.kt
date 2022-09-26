package limber.config

internal data class ConfigImpl(
  override val clock: ClockConfig,
  override val guids: GuidsConfig,
  override val name: String,
  val rest: RestConfig,
  val restClient: RestClientConfig<BaseUrls>,
  override val server: ServerConfig,
  val sql: SqlConfig,
) : Config()
