package kairo.restFeature

import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.connector

internal fun <TConfiguration : ApplicationEngine.Configuration> configureEmbeddedServer(
  config: RestConfig,
): TConfiguration.() -> Unit {
  return {
    applyConnector(config.connector)
    applyLifecycle(config.lifecycle)
    applyParallelism(config.parallelism)
  }
}

private fun ApplicationEngine.Configuration.applyConnector(
  config: RestConfig.Connector,
) {
  connector {
    host = config.host
    port = config.port
  }
}

private fun ApplicationEngine.Configuration.applyLifecycle(
  config: RestConfig.Lifecycle,
) {
  shutdownGracePeriod = config.shutdownGracePeriodMs
  shutdownTimeout = config.shutdownTimeoutMs
}

private fun ApplicationEngine.Configuration.applyParallelism(
  config: RestConfig.Parallelism,
) {
  connectionGroupSize = config.connectionGroupSize
  workerGroupSize = config.workerGroupSize
  callGroupSize = config.callGroupSize
}
