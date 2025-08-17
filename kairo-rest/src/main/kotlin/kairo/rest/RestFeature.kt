package kairo.rest

import io.ktor.server.application.Application
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.netty.NettyApplicationEngine
import kairo.feature.Feature

/**
 * The REST Feature runs a Ktor server for the lifecycle of a Kairo application.
 */
public class RestFeature(
  private val config: RestFeatureConfig,
) : Feature() {
  override val name: String = "REST"

  private var ktorServer: EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration>? = null

  override suspend fun start(features: List<Feature>) {
    val ktorServer = KtorServerFactory.create(config, features)
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
