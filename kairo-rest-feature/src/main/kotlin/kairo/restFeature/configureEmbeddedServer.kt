package kairo.restFeature

import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.connector

internal fun <TConfiguration : ApplicationEngine.Configuration> configureEmbeddedServer(
  config: KairoRestConfig,
): TConfiguration.() -> Unit {
  return {
    applyConnector(config.connector)
    applyLifecycle(config.lifecycle)
    applyParallelism(config.parallelism)
  }
}

private fun ApplicationEngine.Configuration.applyConnector(
  config: KairoRestConfig.Connector,
) {
  connector {
    host = config.host
    port = config.port
  }
}

private fun ApplicationEngine.Configuration.applyLifecycle(
  config: KairoRestConfig.Lifecycle,
) {
  shutdownGracePeriod = config.shutdownGracePeriodMs
  shutdownTimeout = config.shutdownTimeoutMs
}

private fun ApplicationEngine.Configuration.applyParallelism(
  config: KairoRestConfig.Parallelism,
) {
  connectionGroupSize = config.connectionGroupSize
  workerGroupSize = config.workerGroupSize
  callGroupSize = config.callGroupSize
}
