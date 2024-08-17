package kairo.server

import com.google.inject.Injector
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * A Server is an application that runs a set of Features.
 */
public abstract class Server {
  private val logger: KLogger = KotlinLogging.logger {}

  protected abstract val featureManager: FeatureManager

  private val lock: Lock = ReentrantLock()

  private var injector: Injector? = null

  /**
   * Start the Server, binding and starting all Features.
   * If [shutdownHook] is passed, [shutDown] will be automatically called when the JVM shuts down.
   * If [wait] is passed, this call won't return until the JVM is terminated.
   */
  @Suppress("CognitiveComplexMethod", "LongMethod")
  public fun start(shutdownHook: Boolean = true, wait: Boolean = true) {
    lock.withLock {
      if (injector != null) {
        logger.warn { "Server already started." }
        return@start
      }
      logger.info { "Starting Server." }

      try {
        logger.info { "Server Features: $featureManager." }
        val injector = featureManager.createInjector()
        featureManager.start(injector)
        this.injector = injector // Only set the injector property if start() doesn't throw.
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
      val injector = this.injector
      this.injector = null
      featureManager.stop(injector)
    }
  }

  /**
   * [shutDown] will be automatically called when the JVM shuts down.
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
