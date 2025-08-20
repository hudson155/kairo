package kairo.rest

import io.ktor.server.application.Application
import kairo.feature.Feature

/**
 * The REST Feature runs a Ktor server for the lifecycle of a Kairo application.
 */
public class RestFeature(
  private val config: RestFeatureConfig,
  private val ktorConfiguration: KtorServerConfig.() -> Unit = {},
  private val ktorModule: Application.() -> Unit = {},
) : Feature() {
  override val name: String = "REST"

  private var ktorServer: KtorServer? = null

  override suspend fun start(features: List<Feature>) {
    val ktorServer = KtorServerFactory.create(
      config = config,
      features = features,
      ktorConfiguration = ktorConfiguration,
      ktorModule = ktorModule,
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
   * Use this interface on any Features that wish to add Koin bindings.
   */
  public interface HasRouting {
    public fun Application.routing()
  }
}
