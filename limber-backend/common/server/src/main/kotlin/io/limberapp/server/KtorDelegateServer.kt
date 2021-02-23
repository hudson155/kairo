package io.limberapp.server

import io.ktor.application.Application
import io.ktor.application.ApplicationEvents
import io.ktor.application.ApplicationStarted
import io.ktor.application.ApplicationStopPreparing
import io.ktor.application.ApplicationStopping
import io.ktor.server.engine.ApplicationEngineEnvironment
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread
import kotlin.concurrent.withLock
import kotlin.system.exitProcess

private const val SHUT_DOWN_TIMEOUT_SECONDS: Long = 10

/**
 * The delegate server is a small wrapper around the Ktor [Application] class that handles some
 * rudimentary error handling and synchronization.
 */
internal class KtorDelegateServer(
    val application: Application,
    /**
     * Called when the application starts.
     */
    val configure: (Application) -> Unit,
    /**
     * Called when the application stops.
     */
    val cleanUp: () -> Unit,
) {
  private val logger: Logger = LoggerFactory.getLogger(KtorDelegateServer::class.java)

  private val lock: Lock = ReentrantLock(true)

  private var running: Boolean = false

  init {
    with<ApplicationEvents, Unit>(application.environment.monitor) {
      subscribe(ApplicationStarted) { applicationStarted() }
      subscribe(ApplicationStopping) { applicationStopping() }
    }
  }

  private fun applicationStarted() {
    logger.info("Attempting to start server...")
    lock.withLock {
      logger.info("Server starting...")
      if (running) {
        logger.error("Server is already running. Doing nothing.")
        return@withLock
      }
      @Suppress("TooGenericExceptionCaught")
      try {
        running = true
        configure(application)
        logger.info("Server started.")
      } catch (e: Throwable) {
        logger.error("An error occurred during server startup. Shutting down...", e)
        application.shutDown(status = 1, timeoutSeconds = SHUT_DOWN_TIMEOUT_SECONDS)
      }
    }
  }

  private fun applicationStopping() {
    logger.info("Attempting to stop server...")
    lock.withLock {
      logger.info("Server stopping...")
      if (!running) {
        logger.error("Server is already stopped. Doing nothing.")
        return@withLock
      }
      @Suppress("TooGenericExceptionCaught")
      try {
        cleanUp()
      } catch (e: Throwable) {
        logger.error("An error occurred while stopping server.", e)
      }
      running = false
    }
  }

  /**
   * Implementation adapted from [io.ktor.server.engine.ShutDownUrl].
   */
  private fun Application.shutDown(status: Int, timeoutSeconds: Long) {
    val latch = CountDownLatch(1)
    thread {
      latch.await(timeoutSeconds, TimeUnit.SECONDS)
      environment.monitor.raise(ApplicationStopPreparing, environment)
      (environment as? ApplicationEngineEnvironment)?.stop() ?: dispose()
      exitProcess(status)
    }
    latch.countDown()
  }
}
