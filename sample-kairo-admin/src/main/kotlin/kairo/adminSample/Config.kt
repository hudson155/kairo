package kairo.adminSample

import kairo.rest.RestFeatureConfig
import kairo.sql.SqlFeatureConfig

public data class Config(
  val rest: RestFeatureConfig,
  val sql: SqlFeatureConfig,
)
