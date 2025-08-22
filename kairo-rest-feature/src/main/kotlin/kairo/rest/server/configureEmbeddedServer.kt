package kairo.rest.server

import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.connector
import kairo.rest.KairoRestConfig

internal fun <T : ApplicationEngine.Configuration> configureEmbeddedServer(config: KairoRestConfig): T.() -> Unit =
  {
    applyConnector(config.connector)
    applyLifecycle(config.lifecycle)
    applyParallelism(config.parallelism)
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
  config.connectionGroupSize?.let { connectionGroupSize = it }
  config.workerGroupSize?.let { workerGroupSize = it }
  config.callGroupSize?.let { callGroupSize = it }
}
