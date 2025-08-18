package kairo.rest

import io.ktor.server.application.Application
import kairo.feature.Feature

/**
 * The REST Feature runs a Ktor server for the lifecycle of a Kairo application.
 */
public class RestFeature(
  private val config: RestFeatureConfig,
  /**
   * Optionally, you can configure the Ktor server directly.
   * Prefer using provided configuration options through [RestFeatureConfig] when possible.
   */
  private val ktorConfiguration: KtorServerConfig.() -> Unit = {},
) : Feature() {
  override val name: String = "REST"

  private var ktorServer: KtorServer? = null

  override suspend fun start(features: List<Feature>) {
    val ktorServer = KtorServerFactory.create(
      config = config,
      features = features,
      ktorConfiguration = ktorConfiguration,
    )
    this.ktorServer = ktorServer
    ktorServer.start()
  }

  override suspend fun stop() {
    this.ktorServer?.let { ktorServer ->
      ktorServer.stop()
      this.ktorServer = null
    }
  }

  /**
   * Use this interface on any Features that wish to bind routes to the Ktor server.
   */
  public interface HasRouting {
    public fun Application.routing()
  }
}
