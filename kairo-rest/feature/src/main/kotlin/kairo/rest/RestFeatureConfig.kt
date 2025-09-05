package kairo.rest

import io.ktor.http.HttpMethod
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.serialization.Serializable

@Serializable
public data class RestFeatureConfig(
  val parallelism: Parallelism = Parallelism(),
  val timeouts: Timeouts = Timeouts(),
  val lifecycle: Lifecycle = Lifecycle(),
  val connector: Connector,
  val plugins: Plugins,
) {
  @Serializable
  public data class Parallelism(
    val runningLimit: Int = 50,
    val shareWorkGroup: Boolean = false,
    val connectionGroupSize: Int? = 1, // If null, uses Netty's default.
    val workerGroupSize: Int? = 1, // If null, uses Netty's default.
    val callGroupSize: Int? = 10, // If null, uses Netty's default.
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

  @Serializable
  public data class Plugins(
    val autoHeadResponse: AutoHeadResponse? = AutoHeadResponse,
    val callLogging: CallLogging? = CallLogging(),
    val contentNegotiation: ContentNegotiation? = ContentNegotiation,
    val cors: Cors? = null,
    val defaultHeaders: DefaultHeaders?,
    val doubleReceive: DoubleReceive? = null,
    val forwardedHeaders: ForwardedHeaders? = ForwardedHeaders,
  ) {
    @Serializable
    public data object AutoHeadResponse

    @Serializable
    public data class CallLogging(
      val useColors: Boolean = false,
    )

    @Serializable
    public data object ContentNegotiation

    @Serializable
    public data class Cors(
      val hosts: List<Host>,
      val headers: List<String> = emptyList(),
      val methods: List<String> = HttpMethod.DefaultMethods.map { it.value },
      val allowCredentials: Boolean = false,
    ) {
      @Serializable
      public data class Host(
        val host: String,
        val schemes: List<String>,
        val subdomains: List<String> = emptyList(),
      )
    }

    @Serializable
    public data class DefaultHeaders(
      val serverName: String?,
      val headers: Map<String, String> = emptyMap(),
    )

    @Serializable
    public data class DoubleReceive(
      val cacheRawRequest: Boolean,
    )

    @Serializable
    public data object ForwardedHeaders
  }
}
