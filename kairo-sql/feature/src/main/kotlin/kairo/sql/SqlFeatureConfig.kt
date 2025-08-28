package kairo.sql

import kotlinx.serialization.Serializable

@Serializable
public data class SqlFeatureConfig(
  val database: Database,
) {
  @Serializable
  public data class Database(
  ) {
  }
}
