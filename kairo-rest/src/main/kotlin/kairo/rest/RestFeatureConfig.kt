package kairo.rest

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.serialization.Serializable

@Serializable
public data class RestFeatureConfig(
  val connector: Connector,
  val lifecycle: Lifecycle = Lifecycle(),
) {
  @Serializable
  public data class Connector(
    val host: String = "0.0.0.0",
    val port: Int,
  )

  @Serializable
  public data class Lifecycle(
    val shutdownGracePeriod: Duration = Duration.ZERO,
    val shutdownTimeout: Duration = 20.seconds,
  )
}
