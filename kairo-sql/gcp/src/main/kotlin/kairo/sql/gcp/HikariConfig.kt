package kairo.sql.gcp

import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.Option

public fun ConnectionFactoryOptions.Builder.gcpAuth(config: GcpSqlConfig) {
  option(Option.valueOf("socketFactory"), "com.google.cloud.sql.postgres.SocketFactory")
  option(Option.valueOf("cloudSqlInstance"), config.instanceName)
  option(Option.valueOf("enableIamAuth"), "true")
}
