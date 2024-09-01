package kairo.sql

import com.google.inject.Inject
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kairo.dependencyInjection.LazySingletonProvider
import kairo.protectedString.ProtectedString

public class HikariDataSourceProvider @Inject constructor(
  private val config: KairoSqlConfig,
) : LazySingletonProvider<HikariDataSource>() {
  @OptIn(ProtectedString.Access::class)
  override fun create(): HikariDataSource =
    HikariDataSource(
      HikariConfig().apply {
        jdbcUrl = config.jdbcUrl
        config.username?.let { username = it }
        config.password?.let { password = it.value }
        config.properties.forEach { (propertyName, value) ->
          addDataSourceProperty(propertyName, value)
        }
        connectionTimeout = config.connectionTimeoutMs
        minimumIdle = config.minimumIdle
        maximumPoolSize = config.maximumPoolSize
      },
    )
}
