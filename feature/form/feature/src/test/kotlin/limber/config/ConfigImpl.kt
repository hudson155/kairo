package limber.config

import limber.config.sql.SqlConfig

internal data class ConfigImpl(
  val sql: SqlConfig,
) : TestConfig()
