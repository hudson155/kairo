package kairo.server

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Stage
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.feature.Feature
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

private val logger: KLogger = KotlinLogging.logger {}

/**
 * A wrapper around a set of [Feature]s that helps [Server] manage their lifecycle.
 */
public class FeatureManager(
  public val features: Set<Feature>,
  private val config: FeatureManagerConfig,
) {
  internal fun createInjector(): Injector =
    Guice.createInjector(Stage.PRODUCTION, features)

  internal fun start(injector: Injector) {
    runBlocking {
      features.groupBy { it.priority }.toList().sortedBy { it.first.ordinal }
        .forEach { (_, features) ->
          inParallel(features) { feature ->
            logger.info { "Start Feature: ${feature.name}." }
            feature.start(injector)
          }
        }
      Thread.sleep(config.lifecycle.startupDelayMs)

      features.groupBy { it.priority }.toList().sortedBy { it.first.ordinal }
        .forEach { (_, features) ->
          inParallel(features) { feature ->
            logger.info { "AfterStart Feature: ${feature.name}." }
            feature.afterStart(injector)
          }
        }
    }
  }

  internal fun stop(injector: Injector?) {
    runBlocking {
      features.groupBy { it.priority }.toList().sortedBy { it.first.ordinal }
        .forEach { (_, features) ->
          inParallel(features) { feature ->
            logger.info { "BeforeStop Feature: ${feature.name}." }
            feature.beforeStop(injector)
          }
        }
      Thread.sleep(config.lifecycle.shutdownDelayMs)
      features.groupBy { it.priority }.toList().sortedBy { it.first.ordinal }
        .forEach { (_, features) ->
          inParallel(features) { feature ->
            logger.info { "Stop Feature: ${feature.name}." }
            feature.stop(injector)
          }
        }
    }
  }

  override fun toString(): String =
    features.joinToString { it.name }
}

private suspend fun <T> inParallel(list: List<T>, block: (feature: T) -> Unit) {
  val scope = CoroutineScope(Dispatchers.Default)
  list.map { scope.async { block(it) } }.awaitAll()
}
