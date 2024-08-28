package kairo.sql

import com.google.inject.Injector
import com.google.inject.PrivateBinder
import com.zaxxer.hikari.HikariDataSource
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.dependencyInjection.bind
import kairo.dependencyInjection.expose
import kairo.dependencyInjection.getInstance
import kairo.dependencyInjection.toProvider
import kairo.feature.Feature
import kairo.feature.FeaturePriority

private val logger: KLogger = KotlinLogging.logger {}

public class KairoSqlFeature(
  private val config: KairoSqlConfig,
) : Feature() {
  override val name: String = "SQL"

  override val priority: FeaturePriority = FeaturePriority.Framework

  override fun bind(binder: PrivateBinder) {
    binder.bind<KairoSqlConfig>().toInstance(config)

    binder.bind<HikariDataSource>().toProvider(HikariDataSourceProvider::class)
    binder.expose<HikariDataSource>()
  }

  override fun start(injector: Injector, features: Set<Feature>) {
    logger.info { "Getting a connection to initialize the SQL data source." }
    val dataSource = injector.getInstance<HikariDataSource>()
    dataSource.getConnection()
  }

  override fun stop(injector: Injector?) {
    injector ?: return
    logger.info { "Closing the SQL data source." }
    val dataSource = injector.getInstance<HikariDataSource>()
    dataSource.close()
  }
}
