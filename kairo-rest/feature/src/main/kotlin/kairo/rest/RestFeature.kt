package kairo.rest

import io.ktor.server.application.Application
import io.ktor.server.application.ServerReady
import kairo.feature.Feature
import kairo.feature.FeaturePriority
import kairo.feature.LifecycleHandler
import kairo.feature.lifecycle
import kairo.rest.auth.AuthConfig
import kotlinx.coroutines.CompletableDeferred

/**
 * The REST Feature runs a Ktor server for the lifecycle of a Kairo application.
 */
@Suppress("LongParameterList")
public class RestFeature(
  private val config: RestFeatureConfig,
  private val ktorConfig: KtorServerConfig.() -> Unit = {},
  private val authConfig: AuthConfig?,
  private val ktorModule: Application.() -> Unit = {},
) : Feature() {
  override val name: String = "REST"

  private var ktorServer: KtorServer? = null

  override val lifecycle: List<LifecycleHandler> =
    lifecycle {
      handler(FeaturePriority.rest) {
        start { features ->
          val ktorServer = KtorServerFactory.create(
            config = config,
            features = features,
            ktorConfig = ktorConfig,
            authConfig = authConfig,
            ktorModule = ktorModule,
          )
          this@RestFeature.ktorServer = ktorServer
          val ready = CompletableDeferred<Unit>()
          ktorServer.monitor.subscribe(ServerReady) {
            ready.complete(Unit)
          }
          ktorServer.startSuspend()
          ready.await()
        }
        stop { _ ->
          this@RestFeature.ktorServer?.let { ktorServer ->
            ktorServer.stopSuspend()
            this@RestFeature.ktorServer = null
          }
        }
      }
    }

  public companion object
}
