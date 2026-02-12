package kairo.admin

import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigRenderOptions
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
import kairo.dependencyInjection.DependencyInjectionFeature
import kairo.feature.Feature
import kairo.feature.FeaturePriority
import kairo.feature.LifecycleHandler
import kairo.feature.lifecycle
import kairo.healthCheck.HealthCheckFeature
import kairo.mailersend.Mailer
import kairo.mailersend.MailersendFeature
import kairo.rest.HasRouting
import kairo.rest.restEndpointClasses
import kairo.slack.SlackClient
import kairo.slack.SlackFeature
import kairo.stytch.StytchFeature
import org.koin.core.Koin

/**
 * The Admin Dashboard Feature adds a server-rendered admin UI under the configured path prefix.
 * Provides tabs for endpoint exploration, config viewing, JVM monitoring, database browsing,
 * health checks, features, logging, dependencies, integrations, and error tracking.
 *
 * Most dashboard data is auto-discovered from the features list at startup:
 * - Feature names from all registered features
 * - Health checks from [HealthCheckFeature]
 * - Koin/DI from [DependencyInjectionFeature]
 * - ConnectionFactory from Koin
 * - Endpoint classes from Ktor Application attributes (auto-registered by route())
 * - Config sources from CONFIG env var + classpath (if not overridden)
 * - Integrations auto-detected from available ConnectionFactory (if not overridden)
 */
public class AdminDashboardFeature(
  private val config: AdminDashboardConfig = AdminDashboardConfig(),
  private val configSources: List<AdminConfigSource>? = null,
  private val integrations: List<AdminIntegrationInfo>? = null,
) : Feature(), HasRouting {
  override val name: String = "Admin Dashboard"

  public val errorCollector: ErrorCollector = ErrorCollector()

  private var ktorApplication: Application? = null
  private var adminDashboardHandler: AdminDashboardHandler? = null

  override val lifecycle: List<LifecycleHandler> =
    lifecycle {
      handler(FeaturePriority.default) {
        start { features ->
          val featureNames = features.map { it.name }

          val healthChecks = discoverHealthChecks(features)
          val koin = discoverKoin(features)
          val connectionFactory = discoverConnectionFactory(koin)

          val resolvedConfigSources = configSources ?: autoDetectConfigSources()
          val resolvedIntegrations = integrations ?: autoDetectIntegrations(connectionFactory)
          val resolvedConfig = resolveConfig(config)

          val effectiveConfig = generateEffectiveConfig(resolvedConfigSources)

          val slackChannels = discoverSlackChannels(features, koin)
          val stytchModules = discoverStytchModules(features)
          val emailTemplates = discoverEmailTemplates(features, koin)

          adminDashboardHandler = AdminDashboardHandler(
            config = resolvedConfig,
            endpointCollector = EndpointCollector { ktorApplication!!.restEndpointClasses.toList() },
            configCollector = ConfigCollector(resolvedConfigSources, effectiveConfig),
            jvmCollector = JvmCollector(),
            databaseCollector = DatabaseCollector(connectionFactory),
            poolCollector = PoolCollector(connectionFactory),
            featureNames = featureNames,
            healthCheckCollector = HealthCheckCollector(healthChecks),
            loggingCollector = LoggingCollector(),
            integrations = resolvedIntegrations,
            dependencyCollector = DependencyCollector { koin },
            errorCollector = errorCollector,
            slackChannels = slackChannels,
            stytchModules = stytchModules,
            emailTemplates = emailTemplates,
          )
        }
      }
    }

  override fun Application.routing() {
    this@AdminDashboardFeature.ktorApplication = this
    with(adminDashboardHandler!!) { routing() }
  }

  public companion object
}

@Suppress("SwallowedException")
private fun discoverHealthChecks(features: List<Feature>): Map<String, suspend () -> Unit> =
  try {
    val hcFeature = features.filterIsInstance<HealthCheckFeature>().firstOrNull()
      ?: return emptyMap()
    hcFeature.registeredHealthChecks.mapValues { (_, check) ->
      suspend { check.check() }
    }
  } catch (_: NoClassDefFoundError) {
    emptyMap()
  }

@Suppress("SwallowedException")
private fun discoverKoin(features: List<Feature>): Koin? =
  try {
    features.filterIsInstance<DependencyInjectionFeature>().firstOrNull()?.koin
  } catch (_: NoClassDefFoundError) {
    null
  }

private fun discoverConnectionFactory(koin: Koin?): () -> ConnectionFactory? = {
  try {
    koin?.get<ConnectionFactory>()
  } catch (_: Exception) {
    null
  }
}

private fun resolveConfig(config: AdminDashboardConfig): AdminDashboardConfig {
  if (config.kdocsUrl != null) return config
  val kdocsAvailable = Thread.currentThread().contextClassLoader
    .getResource("static/kdocs/index.html") != null
  if (!kdocsAvailable) return config
  return config.copy(kdocsUrl = "/_kdocs/index.html")
}

private fun autoDetectConfigSources(): List<AdminConfigSource> {
  val sources = mutableListOf<AdminConfigSource>()
  val cl = Thread.currentThread().contextClassLoader
  cl.getResource("config/common.conf")?.let {
    sources.add(AdminConfigSource("common", it.readText()))
  }
  val configName = System.getenv("CONFIG")
  if (configName != null) {
    cl.getResource("config/$configName.conf")?.let {
      sources.add(AdminConfigSource(configName, it.readText()))
    }
  }
  return sources
}

@Suppress("SwallowedException")
private fun generateEffectiveConfig(sources: List<AdminConfigSource>): String? =
  try {
    val renderOptions = ConfigRenderOptions.defaults()
      .setOriginComments(false)
      .setComments(false)
      .setJson(false)
    var merged = ConfigFactory.empty()
    for (source in sources) {
      merged = ConfigFactory.parseString(source.content).withFallback(merged)
    }
    merged.resolve().root().render(renderOptions)
  } catch (_: Exception) {
    null
  }

@Suppress("SwallowedException")
private fun discoverSlackChannels(features: List<Feature>, koin: Koin?): Map<String, String>? =
  try {
    features.filterIsInstance<SlackFeature>().firstOrNull() ?: return null
    koin?.get<SlackClient>()?.channels
  } catch (_: NoClassDefFoundError) {
    null
  } catch (_: Exception) {
    null
  }

@Suppress("SwallowedException")
private fun discoverStytchModules(features: List<Feature>): List<String>? =
  try {
    features.filterIsInstance<StytchFeature>().firstOrNull() ?: return null
    listOf(
      "Connected Apps", "Crypto Wallets", "Debug", "Fraud", "IDP",
      "Impersonation", "M2M", "Magic Links", "OAuth", "OTPs",
      "Passwords", "Project", "RBAC", "Sessions", "TOTPs",
      "Users", "WebAuthn",
    )
  } catch (_: NoClassDefFoundError) {
    null
  }

@Suppress("SwallowedException")
private fun discoverEmailTemplates(features: List<Feature>, koin: Koin?): Map<String, String>? =
  try {
    features.filterIsInstance<MailersendFeature>().firstOrNull() ?: return null
    koin?.get<Mailer>()?.templates
  } catch (_: NoClassDefFoundError) {
    null
  } catch (_: Exception) {
    null
  }

private fun autoDetectIntegrations(
  connectionFactory: () -> ConnectionFactory?,
): List<AdminIntegrationInfo> {
  val integrations = mutableListOf<AdminIntegrationInfo>()
  try {
    connectionFactory()?.let {
      integrations.add(
        AdminIntegrationInfo(
          name = "Database",
          type = "SQL Database",
          status = "connected",
        ),
      )
    }
  } catch (_: Exception) {
    // Connection factory not available.
  }
  return integrations
}
