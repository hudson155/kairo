package limber.config.rest

public data class RestConfig(
  /**
   * CORS restrictions will only allow browser requests from hosts in this list.
   */
  val allowedHosts: List<String>,
  val auth: Auth?,
  val parallelization: Parallelization,
  val port: Int,
  val serverName: String,
  val shutDown: ShutDown,
) {
  public data class Auth(
    val jwtClaimPrefix: String,
    val jwtIssuer: String,
    val jwtLeeway: Long,
    val jwkDomain: String,
  )

  /**
   * See the Netty documentation for more detail about how these work.
   */
  public data class Parallelization(
    val connectionGroupSize: Int,
    val workerGroupSize: Int,
    val callGroupSize: Int,
  )

  /**
   * The functionality of this is best described in this comment from Netty, adapted below.
   * https://netty.io/4.0/xref/io/netty/util/concurrent/EventExecutorGroup.html#59.
   *
   * Signals this executor that the caller wants the executor to be shut down.
   * Once this method is called, the executor prepares to shut itself down.
   * Graceful shutdown ensures that no tasks are submitted for the "quiet period" before it shuts itself down.
   * If a task is submitted during the "quiet period", it is guaranteed to be accepted
   * and the "quiet period" will start over.
   */
  public data class ShutDown(
    /**
     * The "quiet period" as described in the documentation.
     */
    val gracePeriodMillis: Long,
    /**
     * The maximum amount of time to wait until the executor is shut down,
     * regardless if a task was submitted during the "quiet period"
     */
    val timeoutMillis: Long,
  )
}
