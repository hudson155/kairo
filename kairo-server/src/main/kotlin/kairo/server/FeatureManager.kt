package kairo.server

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Stage
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.feature.Feature

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
    with(features.sortedBy { it.priority.ordinal }) {
      forEach { feature ->
        logger.info { "Start Feature: ${feature.name}." }
        feature.start(injector)
      }
      Thread.sleep(config.lifecycle.startupDelayMs)
      forEach { feature ->
        logger.info { "AfterStart Feature: ${feature.name}." }
        feature.afterStart(injector)
      }
    }
  }

  internal fun stop(injector: Injector?) {
    with(features.sortedByDescending { it.priority.ordinal }) {
      forEach { feature ->
        logger.info { "BeforeStop Feature: ${feature.name}." }
        feature.beforeStop(injector)
      }
      Thread.sleep(config.lifecycle.shutdownDelayMs)
      forEach { feature ->
        logger.info { "Stop Feature: ${feature.name}." }
        feature.stop(injector)
      }
    }
  }

  override fun toString(): String =
    features.joinToString { it.name }
}
