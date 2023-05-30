package limber.config

import limber.config.auth0.Auth0Config
import limber.config.rest.RestClientConfig
import limber.config.rest.RestConfig
import limber.config.sql.SqlConfig

internal data class MonolithServerConfig(
  val auth0: Auth0Config,
  override val clock: ClockConfig,
  override val ids: IdsConfig,
  override val name: String,
  val rest: RestConfig,
  val restClient: RestClientConfig<BaseUrls>,
  override val server: ServerConfig,
  val sql: SqlConfig,
) : Config()
