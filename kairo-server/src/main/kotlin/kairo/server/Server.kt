package kairo.server

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.feature.Feature
import kairo.feature.LifecycleEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private val logger: KLogger = KotlinLogging.logger {}

/**
 * TODO: Add KDoc.
 */
public class Server(
  private val features: List<Feature>,
) {
  private val lock: Mutex = Mutex()
  public var running: Boolean = false
    private set

  /**
   * Attempts to start the Server.
   * No-op if the Server is already running.
   */
  public suspend fun start() {
    lock.withLock {
      logger.info { "Starting Server." }
      if (running) {
        logger.warn { "Server is already running." }
        return@withLock
      }
      running = true
      try {
        onStart()
      } catch (e: Throwable) {
        running = false
        throw e
      }
    }
  }

  /**
   * Attempts to stop the Server.
   * No-op if the Server is not running.
   */
  public suspend fun stop() {
    lock.withLock {
      logger.info { "Stopping Server." }
      if (!running) {
        logger.warn { "Server is not running." }
        return@withLock
      }
      running = false
      try {
        onStop()
      } catch (e: Throwable) {
        running = true
        throw e
      }
    }
  }

  /**
   * Distributes [LifecycleEvent.Start] to all Features, within a [CoroutineScope].
   */
  private suspend fun onStart() {
    coroutineScope {
      val jobs = features.mapNotNull { feature ->
        with(feature) { on(LifecycleEvent.Start)?.let { Pair(feature, it) } }
      }.associate { it }
      jobs.forEach { (feature, job) ->
        try {
          job.join()
        } catch (e: Throwable) {
          logger.error(e) { "Failed to start Feature (name=${feature.name})." }
          throw e
        }
      }
    }
  }

  /**
   * Distributes [LifecycleEvent.Stop] to all Features, within a [CoroutineScope].
   */
  private suspend fun onStop() {
    supervisorScope {
      val jobs = features.mapNotNull { feature ->
        try {
          with(feature) { on(LifecycleEvent.Stop)?.let { Pair(feature, it) } }
        } catch (e: Throwable) {
          logger.error(e) { "Failed to stop Feature (name=${feature.name})." }
          return@mapNotNull null
        }
      }.associate { it }
      jobs.forEach { (feature, job) ->
        try {
          job.join()
        } catch (e: Throwable) {
          logger.error(e) { "Failed to stop Feature (name=${feature.name})." }
        }
      }
    }
  }
}
