package kairo.rest

import io.ktor.server.application.Application
import io.ktor.server.application.ServerReady
import kairo.feature.Feature
import kotlinx.coroutines.CompletableDeferred

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
    val ready = CompletableDeferred<Unit>()
    ktorServer.monitor.subscribe(ServerReady) {
      ready.complete(Unit)
    }
    ktorServer.start()
    ready.await()
  }

  override suspend fun stop(features: List<Feature>) {
    this.ktorServer?.let { ktorServer ->
      ktorServer.stop()
      this.ktorServer = null
    }
  }
}
