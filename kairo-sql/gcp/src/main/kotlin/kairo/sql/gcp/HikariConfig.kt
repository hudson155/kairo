package kairo.sql.gcp

import com.zaxxer.hikari.HikariConfig

public fun HikariConfig.gcpAuth(config: GcpSqlConfig) {
  addDataSourceProperty("socketFactory", "com.google.cloud.sql.postgres.SocketFactory")
  addDataSourceProperty("cloudSqlInstance", config.instanceName)
  addDataSourceProperty("enableIamAuth", "true")
}
