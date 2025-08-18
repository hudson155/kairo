package kairo.rest

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.serialization.Serializable

@Serializable
public data class RestFeatureConfig(
  val parallelism: Parallelism = Parallelism(),
  val timeouts: Timeouts = Timeouts(),
  val lifecycle: Lifecycle = Lifecycle(),
  val connector: Connector,
) {
  @Serializable
  public data class Parallelism(
    val runningLimit: Int = 50,
    val shareWorkGroup: Boolean = false,
    val connectionGroupSize: Int? = 1,
    val workerGroupSize: Int? = 1,
    val callGroupSize: Int? = 10,
  )

  @Serializable
  public data class Timeouts(
    val requestRead: Duration = 2.seconds,
    val responseWrite: Duration = 2.seconds,
  )

  @Serializable
  public data class Lifecycle(
    val shutdownGracePeriod: Duration = Duration.ZERO,
    val shutdownTimeout: Duration = 15.seconds,
  )

  @Serializable
  public data class Connector(
    val host: String = "0.0.0.0",
    val port: Int,
  )
}
