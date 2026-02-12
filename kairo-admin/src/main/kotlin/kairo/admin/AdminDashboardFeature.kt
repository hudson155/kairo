package kairo.admin

import io.ktor.server.application.Application
import io.r2dbc.spi.ConnectionFactory
import kairo.admin.collector.ConfigCollector
import kairo.admin.collector.DatabaseCollector
import kairo.admin.collector.EndpointCollector
import kairo.admin.collector.JvmCollector
import kairo.admin.handler.AdminDashboardHandler
import kairo.feature.Feature
import kairo.rest.HasRouting
import kairo.rest.RestEndpoint
import kairo.rest.auth.AuthReceiver
import kairo.rest.auth.public
import kotlin.reflect.KClass

/**
 * The Admin Dashboard Feature adds a server-rendered admin UI under the configured path prefix.
 * Provides tabs for endpoint exploration, config viewing, JVM monitoring, and database browsing.
 */
@Suppress("LongParameterList")
public class AdminDashboardFeature(
  private val config: AdminDashboardConfig = AdminDashboardConfig(),
  @Suppress("UnusedPrivateProperty")
  private val auth: suspend AuthReceiver<*>.() -> Unit = { public() },
  configSources: List<AdminConfigSource> = emptyList(),
  endpointClasses: List<KClass<out RestEndpoint<*, *>>> = emptyList(),
  connectionFactory: () -> ConnectionFactory? = { null },
) : Feature(), HasRouting {
  override val name: String = "Admin Dashboard"

  private val endpointCollector: EndpointCollector = EndpointCollector(endpointClasses)
  private val configCollector: ConfigCollector = ConfigCollector(configSources)
  private val jvmCollector: JvmCollector = JvmCollector()
  private val databaseCollector: DatabaseCollector = DatabaseCollector(connectionFactory)

  private val adminDashboardHandler: AdminDashboardHandler = AdminDashboardHandler(
    config = config,
    endpointCollector = endpointCollector,
    configCollector = configCollector,
    jvmCollector = jvmCollector,
    databaseCollector = databaseCollector,
  )

  override fun Application.routing() {
    with(adminDashboardHandler) { routing() }
  }

  public companion object
}
