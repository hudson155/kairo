package kairo.sql

import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.Singleton
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kairo.dependencyInjection.LazySingletonProvider
import kairo.dependencyInjection.getNamedInstance
import kairo.protectedString.ProtectedString

/**
 * There is a single global [HikariDataSource] instance in Kairo.
 */
@Singleton
public class HikariDataSourceProvider(
  private val name: String,
) : LazySingletonProvider<HikariDataSource>() {
  @Inject
  private lateinit var injector: Injector

  private val config: KairoSqlConfig by lazy { injector.getNamedInstance(name) }

  @OptIn(ProtectedString.Access::class)
  override fun create(): HikariDataSource =
    HikariDataSource(
      HikariConfig().apply {
        jdbcUrl = config.jdbcUrl
        config.schema?.let { schema = it }
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
