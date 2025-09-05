package kairo.rest

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.plugins.doublereceive.DoubleReceive
import io.ktor.server.plugins.forwardedheaders.ForwardedHeaders
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import kairo.exception.LogicalFailure
import kairo.feature.Feature

private val logger: KLogger = KotlinLogging.logger {}

internal object KtorServerFactory {
  @Suppress("LongParameterList")
  fun create(
    config: RestFeatureConfig,
    features: List<Feature>,
    ktorConfiguration: KtorServerConfig.() -> Unit,
    ktorModule: Application.() -> Unit,
  ): KtorServer =
    embeddedServer(
      factory = Netty,
      environment = applicationEnvironment(),
      configure = {
        configure(
          parallelism = config.parallelism,
          timeouts = config.timeouts,
          lifecycle = config.lifecycle,
          connector = config.connector,
        )
        ktorConfiguration()
      },
      module = {
        plugins(config.plugins)
        routing(features)
        ktorModule()
      },
    )

  @Suppress("LongParameterList")
  private fun KtorServerConfig.configure(
    parallelism: RestFeatureConfig.Parallelism,
    timeouts: RestFeatureConfig.Timeouts,
    lifecycle: RestFeatureConfig.Lifecycle,
    connector: RestFeatureConfig.Connector,
  ) {
    runningLimit = parallelism.runningLimit
    shareWorkGroup = parallelism.shareWorkGroup
    parallelism.connectionGroupSize?.let { connectionGroupSize = it }
    parallelism.workerGroupSize?.let { workerGroupSize = it }
    parallelism.callGroupSize?.let { callGroupSize = it }
    requestReadTimeoutSeconds = timeouts.requestRead.inWholeSeconds.toInt()
    responseWriteTimeoutSeconds = timeouts.responseWrite.inWholeSeconds.toInt()
    maxInitialLineLength = 8 * 1024
    maxHeaderSize = 16 * 1024
    maxChunkSize = 8 * 1024
    shutdownGracePeriod = lifecycle.shutdownGracePeriod.inWholeMilliseconds
    shutdownTimeout = lifecycle.shutdownTimeout.inWholeMilliseconds
    connector {
      host = connector.host
      port = connector.port
    }
  }

  private fun Application.plugins(config: RestFeatureConfig.Plugins) {
    // TODO: Mention plugins in changelog.
    installAutoHeadResponse(config.autoHeadResponse)
    installCallLogging(config.callLogging)
    installContentNegotiation(config.contentNegotiation)
    installCors(config.cors)
    installDefaultHeaders(config.defaultHeaders)
    installDoubleReceive(config.doubleReceive)
    installForwardedHeaders(config.forwardedHeaders)
    installStatusPages()
  }

  private fun Application.installAutoHeadResponse(config: RestFeatureConfig.Plugins.AutoHeadResponse?) {
    config ?: return
    install(AutoHeadResponse)
  }

  private fun Application.installCallLogging(config: RestFeatureConfig.Plugins.CallLogging?) {
    config ?: return
    install(CallLogging) {
      if (!config.useColors) disableDefaultColors()
    }
  }

  private fun Application.installContentNegotiation(config: RestFeatureConfig.Plugins.ContentNegotiation?) {
    config ?: return
    install(ContentNegotiation) {
      json()
    }
  }

  private fun Application.installCors(config: RestFeatureConfig.Plugins.Cors?) {
    config ?: return
    install(CORS) {
      config.hosts.forEach { host ->
        allowHost(
          host = host.host,
          schemes = host.schemes,
          subDomains = host.subdomains,
        )
      }
      config.headers.forEach { header -> allowHeader(header) }
      config.methods.forEach { method -> allowMethod(HttpMethod.parse(method)) }
      allowCredentials = config.allowCredentials
    }
  }

  private fun Application.installDefaultHeaders(config: RestFeatureConfig.Plugins.DefaultHeaders?) {
    config ?: return
    install(DefaultHeaders) {
      config.serverName?.let { header(HttpHeaders.Server, it) }
      config.headers.forEach { header(it.key, it.value) }
    }
  }

  private fun Application.installDoubleReceive(config: RestFeatureConfig.Plugins.DoubleReceive?) {
    config ?: return
    install(DoubleReceive) {
      cacheRawRequest = config.cacheRawRequest
    }
  }

  private fun Application.installForwardedHeaders(config: RestFeatureConfig.Plugins.ForwardedHeaders?) {
    config ?: return
    install(ForwardedHeaders)
  }

  private fun Application.installStatusPages() {
    install(StatusPages) {
      exception<LogicalFailure> { call, cause ->
        call.response.status(cause.status)
        call.respond(cause.json)
      }
    }
  }

  private fun Application.routing(features: List<Feature>) {
    features.forEach { feature ->
      if (feature !is HasRouting) return@forEach
      logger.info { "Registering routes (featureName=${feature.name})." }
      with(feature) { routing() }
    }
  }
}
