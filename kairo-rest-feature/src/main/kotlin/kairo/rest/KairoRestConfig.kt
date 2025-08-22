package kairo.rest

import io.ktor.server.engine.ApplicationEngine

public data class KairoRestConfig(
  val connector: Connector,
  val lifecycle: Lifecycle,
  val parallelism: Parallelism,
) {
  /**
   * [host] and [port] specify where the server should listen.
   * [host] is typically "0.0.0.0".
   */
  public data class Connector(
    val host: String,
    val port: Int,
  )

  /**
   * [shutdownGracePeriodMs] and [shutdownTimeoutMs] add extra buffer time during shutdown.
   * See [ApplicationEngine.Configuration].
   */
  public data class Lifecycle(
    val shutdownGracePeriodMs: Long,
    val shutdownTimeoutMs: Long,
  )

  /**
   * See [ApplicationEngine.Configuration].
   */
  public data class Parallelism(
    val connectionGroupSize: Int?,
    val workerGroupSize: Int?,
    val callGroupSize: Int?,
  )
}
