package kairo.rest

import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.routing.Routing
import kairo.feature.Feature

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

  public interface HasRouting {
    public fun Routing.routing()
  }
}
