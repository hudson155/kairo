package limber.config

import limber.config.sql.EventConfig
import limber.config.sql.SqlConfig

internal data class ConfigImpl(
  val event: EventConfig,
  val sql: SqlConfig,
) : TestConfig()
