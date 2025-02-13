package kairo.sqlMigration

import com.google.inject.Binder
import com.google.inject.Injector
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.dependencyInjection.bind
import kairo.dependencyInjection.getNamedInstance
import kairo.dependencyInjection.named
import kairo.feature.Feature
import kairo.feature.FeaturePriority
import org.flywaydb.core.Flyway

private val logger: KLogger = KotlinLogging.logger {}

public open class KairoSqlMigrationFeature(
  private val config: KairoSqlMigrationConfig,
) : Feature() {
  final override val name: String = "SQL Migration"

  final override val priority: FeaturePriority = FeaturePriority.Framework

  override fun bind(binder: Binder) {
    binder.bind<KairoSqlMigrationConfig>().named(config.name).toInstance(config)
    binder.bind<Flyway>().named(config.name).toProvider(FlywayProvider(config.name))
  }

  override fun start(injector: Injector) {
    if (!config.run) {
      logger.info { "Skipping SQL migrations." }
      return
    }
    logger.info { "Running SQL migrations." }
    val flyway = injector.getNamedInstance<Flyway>(config.name)
    flyway.migrate()
  }
}
