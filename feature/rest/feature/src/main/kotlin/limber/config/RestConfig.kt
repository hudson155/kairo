package limber.config

public data class RestConfig(
  val parallelization: Parallelization,
  val port: Int,
  val shutDown: ShutDown,
) {
  public data class Parallelization(
    val connectionGroupSize: Int,
    val workerGroupSize: Int,
    val callGroupSize: Int,
  )

  public data class ShutDown(
    val gracePeriodMillis: Long,
    val timeoutMillis: Long,
  )
}
