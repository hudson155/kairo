package kairo.sql

import com.google.inject.Binder
import com.google.inject.Injector
import com.zaxxer.hikari.HikariDataSource
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.dependencyInjection.bind
import kairo.dependencyInjection.getNamedInstance
import kairo.dependencyInjection.named
import kairo.feature.Feature
import kairo.feature.FeaturePriority
import org.jdbi.v3.core.Jdbi

private val logger: KLogger = KotlinLogging.logger {}

public open class KairoSqlFeature(
  private val config: KairoSqlConfig,
  private val configureJdbi: (jdbi: Jdbi) -> Unit = { it.applyKairoPlugins() },
) : Feature() {
  final override val name: String = "SQL"

  final override val priority: FeaturePriority = FeaturePriority.Framework

  override fun bind(binder: Binder) {
    binder.bindSql(config, configureJdbi)
  }

  override fun start(injector: Injector) {
    logger.info { "Getting a connection to initialize the SQL data source." }
    val dataSource = injector.getNamedInstance<HikariDataSource>(config.name)
    dataSource.getConnection()
  }

  override fun stop(injector: Injector?) {
    injector ?: return
    logger.info { "Closing the SQL data source." }
    val dataSource = injector.getNamedInstance<HikariDataSource>(config.name)
    dataSource.close()
  }
}

public fun Binder.bindSql(
  config: KairoSqlConfig,
  configureJdbi: (jdbi: Jdbi) -> Unit = { it.applyKairoPlugins() },
) {
  bind<KairoSqlConfig>().named(config.name).toInstance(config)
  bind<HikariDataSource>().named(config.name).toProvider(HikariDataSourceProvider(config.name))
  bind<Jdbi>().named(config.name).toProvider(JdbiProvider(config.name, configureJdbi))
  bind<Sql>().named(config.name).toProvider(SqlProvider(config.name))
  bind<SqlTransaction>().named(config.name).toProvider(SqlTransactionProvider(config.name))
}
