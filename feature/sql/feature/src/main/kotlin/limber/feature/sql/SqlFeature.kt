package limber.feature.sql

import com.google.inject.Injector
import com.google.inject.PrivateBinder
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import limber.config.sql.SqlConfig
import limber.feature.Feature
import limber.feature.FeaturePriority
import mu.KLogger
import mu.KotlinLogging
import org.flywaydb.core.Flyway
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import javax.sql.DataSource

public open class SqlFeature(private val config: SqlConfig) : Feature() {
  private val logger: KLogger = KotlinLogging.logger {}

  final override val priority: FeaturePriority = FeaturePriority.Framework

  protected var dataSource: HikariDataSource? = null

  final override fun bind(binder: PrivateBinder) {
    bindDataSource()
    bindJdbi()
  }

  private fun bindDataSource() {
    logger.info { "Creating SQL data source..." }
    dataSource = HikariDataSource(
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
    bind(DataSource::class.java).toInstance(dataSource)
  }

  private fun bindJdbi() {
    val jdbi = Jdbi.create(dataSource).apply {
      installPlugin(KotlinPlugin())
      installPlugin(PostgresPlugin())
    }
    bind(Jdbi::class.java).toInstance(jdbi)
    expose(Jdbi::class.java)
  }

  final override fun start(injector: Injector, features: Set<Feature>) {
    if (config.migrations.run) runMigrations()
  }

  @Suppress("SpreadOperator") // This is okay during startup.
  private fun runMigrations() {
    logger.info { "Running SQL migrations..." }
    val flyway = Flyway.configure()
      .cleanOnValidationError(config.migrations.cleanOnValidationError)
      .cleanDisabled(config.migrations.cleanDisabled)
      .locations(*config.migrations.locations.toTypedArray())
      .defaultSchema(config.migrations.defaultSchema)
      .schemas(*config.migrations.schemas.toTypedArray())
      .dataSource(dataSource)
      .load()
    flyway.migrate()
  }

  final override fun stop() {
    logger.info { "Closing SQL data source..." }
    dataSource?.close()
  }
}
