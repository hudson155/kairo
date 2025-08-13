package kairo.server

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.feature.Feature
import kairo.feature.LifecycleEvent
import kotlin.time.measureTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
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

  @Volatile
  public var state: ServerState = ServerState.Default
    private set

  /**
   * Attempts to start the Server.
   * No-op if the Server is already running.
   */
  public suspend fun start() {
    lock.withLock {
      logger.info { "Starting Server." }
      val time = measureTime {
        check(state == ServerState.Default) { "Server cannot start (state=$state)." }
        state = ServerState.Starting
        try {
          onStart()
          state = ServerState.Running
        } catch (e: Throwable) {
          logger.warn { "Server failed to start." }
          state = ServerState.Default
          throw e
        }
      }
      logger.info { "Server started (time=$time)." }
    }
  }

  /**
   * Attempts to stop the Server.
   * No-op if the Server is not running.
   */
  public suspend fun stop() {
    lock.withLock {
      logger.info { "Stopping Server." }
      if (state != ServerState.Running) {
        logger.warn { "Server cannot stop (state=$state)." }
        return@withLock
      }
      state = ServerState.Stopping
      try {
        onStop()
      } catch (e: Throwable) {
        running = true
        throw e
      }
    }
  }

  /**
   * Distributes [LifecycleEvent.Start] to all Features.
   * This happens concurrently, within a [CoroutineScope].
   */
  private suspend fun onStart() {
    coroutineScope {
      val jobs = features.map { feature ->
        launch(Dispatchers.IO) {
          try {
            logger.info { "Starting Feature (name=${feature.name})." }
            val time = measureTime {
              feature.on(LifecycleEvent.Start)
            }
            logger.info { "Feature started (name=${feature.name}, time=$time)." }
          } catch (e: Throwable) {
            logger.error(e) { "Feature failed to start (name=${feature.name})." }
            throw e
          }
        }
      }
      jobs.joinAll()
    }
  }

  /**
   * Distributes [LifecycleEvent.Stop] to all Features.
   * This happens concurrently, within a [CoroutineScope].
   */
  private suspend fun onStop() {
    supervisorScope {
      val jobs = features.map { feature ->
        launch(Dispatchers.IO)
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
