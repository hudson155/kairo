package limber.sql

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Injector
import com.google.inject.PrivateBinder
import com.google.inject.name.Names
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import limber.config.SqlConfig
import limber.feature.Feature
import limber.feature.FeaturePriority
import limber.serialization.ObjectMapperFactory
import mu.KLogger
import mu.KotlinLogging
import org.flywaydb.core.Flyway
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.jackson2.Jackson2Config
import org.jdbi.v3.jackson2.Jackson2Plugin
import org.jdbi.v3.postgres.PostgresPlugin
import javax.sql.DataSource

internal const val SQL_OBJECT_MAPPER = "SQL_OBJECT_MAPPER"

public open class SqlFeature(private val config: SqlConfig) : Feature() {
  private val logger: KLogger = KotlinLogging.logger {}

  final override val priority: FeaturePriority = FeaturePriority.Framework

  protected var dataSource: HikariDataSource? = null

  final override fun bind(binder: PrivateBinder) {
    val objectMapper = bindObjectMapper()
    bindDataSource()
    bindJdbi(objectMapper)
  }

  private fun bindObjectMapper(): ObjectMapper {
    val objectMapper: ObjectMapper = ObjectMapperFactory.builder(ObjectMapperFactory.Format.JSON).build()
    bind(ObjectMapper::class.java).annotatedWith(Names.named(SQL_OBJECT_MAPPER)).toInstance(objectMapper)
    expose(ObjectMapper::class.java).annotatedWith(Names.named(SQL_OBJECT_MAPPER))
    return objectMapper
  }

  private fun bindDataSource() {
    logger.info { "Creating SQL data source..." }
    dataSource = HikariDataSource(
      HikariConfig().apply {
        jdbcUrl = config.jdbcUrl
        config.username?.let { username = it }
        config.password?.let { password = it.value }
        connectionTimeout = config.connectionTimeoutMs
        minimumIdle = config.minimumIdle
        maximumPoolSize = config.maximumPoolSize
      },
    )
    bind(DataSource::class.java).toInstance(dataSource)
  }

  private fun bindJdbi(objectMapper: ObjectMapper) {
    val jdbi = Jdbi.create(dataSource).apply {
      installPlugin(Jackson2Plugin())
      with(getConfig(Jackson2Config::class.java)) {
        mapper = objectMapper
      }
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
      .locations(*config.migrations.locations.toTypedArray())
      .defaultSchema(config.migrations.defaultSchema)
      .schemas(*config.migrations.schemas.toTypedArray())
      .dataSource(dataSource)
      .load()
    check(flyway.migrate().success) { "Migration was unsuccessful." }
  }

  final override fun stop() {
    logger.info { "Closing SQL data source..." }
    dataSource?.close()
  }
}
