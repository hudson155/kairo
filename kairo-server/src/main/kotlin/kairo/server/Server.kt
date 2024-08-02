package kairo.server

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Stage
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kairo.feature.Feature
import kotlin.concurrent.withLock

/**
 * A Server is an application that runs a set of Features.
 */
public abstract class Server(
  private val config: ServerConfig
) {
  private val logger: KLogger = KotlinLogging.logger {}

  public abstract val features: Set<Feature>

  private val lock: Lock = ReentrantLock()

  private var injector: Injector? = null

  /**
   * Start the Server, binding and starting all Features.
   * If [shutdownHook] is passed, [shutDown] will be automatically called when the JVM shuts down.
   * If [wait] is passed, this call won't return until the JVM is terminated.
   */
  public fun start(shutdownHook: Boolean = true, wait: Boolean = true) {
    lock.withLock {
      if (injector != null) {
        logger.warn { "Server already started." }
        return@start
      }
      logger.info { "Starting Server." }

      try {
        logger.info { "Server Features: ${features.joinToString { it.name }}." }
        val injector = Guice.createInjector(Stage.PRODUCTION, features)
        startFeatures(injector)
        this.injector = injector
      } catch (startException: Exception) {
        logger.error(startException) { "Server startup failed. Shutting down." }
        try {
          shutDown()
        } catch (shutDownException: Exception) {
          logger.error(shutDownException) { "Server shutdown failed." }
        }
        throw startException
      }
    }

    if (shutdownHook) {
      addShutdownHook()
    }

    if (wait) {
      waitForJvmTermination()
    }
  }

  /**
   * Shuts down the Server, stopping all Features.
   */
  public fun shutDown() {
    lock.withLock {
      logger.info { "Shutting down Server." }
      stopFeatures(injector)
      injector = null
    }
  }

  private fun startFeatures(injector: Injector) {
    with(features.sortedBy { it.priority.ordinal }) {
      forEach { feature ->
        logger.info { "Server start Feature: ${feature.name}." }
        feature.start(injector, features)
      }
      Thread.sleep(config.lifecycle.startupDelayMs)
      forEach { feature ->
        logger.info { "Server afterStart Feature: ${feature.name}." }
        feature.afterStart(injector)
      }
    }
  }

  private fun stopFeatures(injector: Injector?) {
    with(features.sortedByDescending { it.priority.ordinal }) {
      forEach { feature ->
        logger.info { "Server beforeStop Feature: ${feature.name}." }
        feature.beforeStop(injector)
      }
      Thread.sleep(config.lifecycle.shutdownDelayMs)
      forEach { feature ->
        logger.info { "Server stop Feature: ${feature.name}." }
        feature.stop(injector)
      }
    }
  }

  /**
   * [shutDown] will be automatically called when the JVM shuts down
   */
  private fun addShutdownHook() {
    Runtime.getRuntime().addShutdownHook(Thread { shutDown() })
  }

  /**
   * Deadlocks the current thread until JVM termination.
   */
  private fun waitForJvmTermination() {
    logger.info { "Server will wait for JVM to terminate." }
    Thread.currentThread().join()
  }
}
