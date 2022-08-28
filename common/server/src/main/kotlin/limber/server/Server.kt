package limber.server

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module
import com.google.inject.Stage
import limber.config.Config
import limber.feature.Feature
import mu.KotlinLogging

/**
 * A Server runs a collection of Features.
 *
 * See the root README for more detail.
 */
public abstract class Server<C : Config>(private val config: C) {
  private val logger = KotlinLogging.logger {}

  public abstract val features: Set<Feature>

  public var injector: Injector? = null

  /**
   * Start the Server, binding and starting all Features.
   *
   * If [wait] is passed, this call won't return until the JVM is terminated,
   * at which time [stop] will have already been called.
   */
  @Synchronized
  public fun start(wait: Boolean = true) {
    check(injector == null)
    logger.info { "Starting Server: ${config.name}." }

    @Suppress("TooGenericExceptionCaught") // During startup we're okay catching a generic exception.
    try {
      logger.info { "Server Features: ${features.joinToString { it.name }}." }
      val modules: Set<Module> = features + ServerModule(config)
      val injector = Guice.createInjector(Stage.PRODUCTION, modules)
      startFeatures(injector)
      this.injector = injector
    } catch (e: Exception) {
      logger.error(e) { "Server startup failed." }
      stop()
    }

    if (wait) {
      waitForJvmTermination()
    }
  }

  /**
   * Stops the Server.
   */
  @Synchronized
  public fun stop() {
    logger.info { "Stopping Server: ${config.name}." }
    stopFeatures(injector)
    injector = null
  }

  private fun startFeatures(injector: Injector) {
    with(features.sortedBy { it.priority.ordinal }) {
      forEach { feature ->
        logger.info { "Starting start Feature: ${feature.name}." }
        feature.start(injector, features)
      }
      Thread.sleep(config.server.startupDelayMs)
      forEach { feature ->
        logger.info { "Server afterStart Feature: ${feature.name}." }
        feature.afterStart(injector)
      }
    }
  }

  private fun stopFeatures(injector: Injector?) {
    with(features.sortedByDescending { it.priority.ordinal }) {
      forEach { feature ->
        logger.info { "Server stop Feature: ${feature.name}." }
        feature.beforeStop(injector)
      }
      Thread.sleep(config.server.shutdownDelayMs)
      forEach { feature ->
        logger.info { "Server stop Feature: ${feature.name}." }
        feature.stop()
      }
    }
  }

  private fun waitForJvmTermination() {
    logger.info { "Server will wait for JVM to terminate." }
    Runtime.getRuntime().removeShutdownHook(Thread { stop() })
    Thread.currentThread().join() // Deadlocks the current thread until JVM termination.
  }
}
