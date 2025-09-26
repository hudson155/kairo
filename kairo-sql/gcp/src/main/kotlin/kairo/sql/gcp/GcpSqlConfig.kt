package kairo.sql.gcp

import kotlinx.serialization.Serializable

@Serializable
public data class GcpSqlConfig(
  val instanceName: String,
)
