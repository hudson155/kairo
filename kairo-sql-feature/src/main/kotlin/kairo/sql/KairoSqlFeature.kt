package kairo.sql

import com.google.inject.Binder
import com.google.inject.Injector
import com.zaxxer.hikari.HikariDataSource
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.dependencyInjection.bind
import kairo.dependencyInjection.getInstance
import kairo.dependencyInjection.toProvider
import kairo.feature.Feature
import kairo.feature.FeaturePriority
import org.jdbi.v3.core.Jdbi

private val logger: KLogger = KotlinLogging.logger {}

public open class KairoSqlFeature(
  private val config: KairoSqlConfig,
) : Feature() {
  override val name: String = "SQL"

  override val priority: FeaturePriority = FeaturePriority.Framework

  override fun bind(binder: Binder) {
    binder.bind<KairoSqlConfig>().toInstance(config)
    binder.bind<HikariDataSource>().toProvider(HikariDataSourceProvider::class)
    binder.bind<Jdbi>().toProvider(JdbiProvider::class)
  }

  override fun start(injector: Injector, features: Set<Feature>) {
    logger.info { "Getting a connection to initialize the SQL data source." }
    val dataSource = injector.getInstance<HikariDataSource>()
    @Suppress("UsePropertyAccessSyntax")
    dataSource.getConnection()
  }

  override fun stop(injector: Injector?) {
    injector ?: return
    logger.info { "Closing the SQL data source." }
    val dataSource = injector.getInstance<HikariDataSource>()
    dataSource.close()
  }
}
