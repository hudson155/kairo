package kairo.admin

import io.ktor.server.application.Application
import io.r2dbc.spi.ConnectionFactory
import kairo.admin.collector.ConfigCollector
import kairo.admin.collector.DatabaseCollector
import kairo.admin.collector.DependencyCollector
import kairo.admin.collector.EndpointCollector
import kairo.admin.collector.ErrorCollector
import kairo.admin.collector.HealthCheckCollector
import kairo.admin.collector.JvmCollector
import kairo.admin.collector.LoggingCollector
import kairo.admin.collector.PoolCollector
import kairo.admin.handler.AdminDashboardHandler
import kairo.admin.model.AdminIntegrationInfo
import kairo.feature.Feature
import kairo.rest.HasRouting
import kairo.rest.RestEndpoint
import kairo.rest.auth.AuthReceiver
import kairo.rest.auth.public
import kotlin.reflect.KClass
import org.koin.core.Koin

/**
 * The Admin Dashboard Feature adds a server-rendered admin UI under the configured path prefix.
 * Provides tabs for endpoint exploration, config viewing, JVM monitoring, database browsing,
 * health checks, features, logging, dependencies, integrations, and error tracking.
 */
@Suppress("LongParameterList")
public class AdminDashboardFeature(
  private val config: AdminDashboardConfig = AdminDashboardConfig(),
  @Suppress("UnusedPrivateProperty")
  private val auth: suspend AuthReceiver<*>.() -> Unit = { public() },
  configSources: List<AdminConfigSource> = emptyList(),
  endpointClasses: List<KClass<out RestEndpoint<*, *>>> = emptyList(),
  connectionFactory: () -> ConnectionFactory? = { null },
  featureNames: List<String> = emptyList(),
  healthChecks: Map<String, suspend () -> Unit> = emptyMap(),
  integrations: List<AdminIntegrationInfo> = emptyList(),
  koinProvider: () -> Koin? = { null },
) : Feature(), HasRouting {
  override val name: String = "Admin Dashboard"

  public val errorCollector: ErrorCollector = ErrorCollector()

  private val adminDashboardHandler: AdminDashboardHandler = AdminDashboardHandler(
    config = config,
    endpointCollector = EndpointCollector(endpointClasses),
    configCollector = ConfigCollector(configSources),
    jvmCollector = JvmCollector(),
    databaseCollector = DatabaseCollector(connectionFactory),
    poolCollector = PoolCollector(connectionFactory),
    featureNames = featureNames,
    healthCheckCollector = HealthCheckCollector(healthChecks),
    loggingCollector = LoggingCollector(),
    integrations = integrations,
    dependencyCollector = DependencyCollector(koinProvider),
    errorCollector = errorCollector,
  )

  override fun Application.routing() {
    with(adminDashboardHandler) { routing() }
  }

  public companion object
}
