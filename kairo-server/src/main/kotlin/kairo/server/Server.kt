package kairo.server

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.feature.Feature
import kotlin.time.measureTime
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private val logger: KLogger = KotlinLogging.logger {}

/**
 * A Kairo Server combines [Feature]s with arbitrary functionality,
 * running them all together.
 */
public class Server(
  private val name: String,
  private val features: List<Feature>,
) {
  private val lock: Mutex = Mutex()

  @Volatile // Allows reads without taking the lock.
  public var state: ServerState = ServerState.Default
    private set

  /**
   * Attempts to start the Server.
   * If this method returns, the Server started successfully.
   * If this method throws, the Server failed to start.
   */
  public suspend fun start() {
    logger.info { "Starting Server (server=$this)." }
    val time = measureTime {
      lock.withLock {
        check(state == ServerState.Default) { "Server cannot start (server=$this)." }
        state = ServerState.Starting
        try {
          onStart()
          state = ServerState.Running
        } catch (startException: Throwable) {
          // Attempt to stop Features.
          logger.warn(startException) { "Server failed to start (server=$this)." }
          try {
            onStop()
          } catch (stopException: Throwable) {
            startException.addSuppressed(stopException)
          } finally {
            state = ServerState.Default
          }
          throw startException
        }
      }
    }
    logger.info { "Server started (server=$this, time=$time)." }
  }

  /**
   * Stops the Server.
   * This method never throws.
   */
  public suspend fun stop() {
    logger.info { "Stopping Server (server=$this)." }
    val time = measureTime {
      lock.withLock {
        check(state == ServerState.Running) { "Server cannot stop (server=$this)." }
        state = ServerState.Stopping
        try {
          onStop()
        } catch (e: Throwable) {
          logger.warn(e) { "Server failed to stop (server=$this)." }
          throw e
        } finally {
          state = ServerState.Default
        }
      }
    }
    logger.info { "Server stopped (server=$this, time=$time)." }
  }

  /**
   * Starts all Features IN PARALLEL.
   * If any Feature fails to start, all [Feature.start] calls are canceled and this method throws.
   */
  private suspend fun onStart() {
    coroutineScope {
      val jobs = features.map { feature ->
        launch {
          logger.info { "Starting Feature (server=${this@Server}, feature=$feature)." }
          val time = measureTime {
            try {
              feature.start(features)
            } catch (e: Throwable) {
              logger.warn(e) { "Feature failed to start (server=${this@Server}, feature=$feature)." }
              throw e
            }
          }
          logger.info { "Feature started (server=${this@Server}, feature=$feature, time=$time)." }
        }
      }
      jobs.joinAll()
    }
    features.forEach { feature ->
      feature.afterStart()
    }
  }

  /**
   * Stops all Features IN PARALLEL.
   * If any Feature fails to stop, remaining [Feature.stop] calls are allowed to complete. This method does not throw.
   */
  private suspend fun onStop() {
    features.forEach { feature ->
      try {
        feature.beforeStop()
      } catch (e: Throwable) {
        logger.warn(e) { "Feature failed beforeStop (server=${this@Server}, feature=$feature)." }
      }
    }
    supervisorScope {
      val jobs = features.map { feature ->
        launch {
          logger.info { "Stopping Feature (server=${this@Server}, feature=$feature)." }
          val time = measureTime {
            try {
              feature.stop()
            } catch (e: Throwable) {
              logger.error(e) { "Feature failed to stop (server=${this@Server}, feature=$feature)." }
            }
          }
          logger.info { "Feature stopped (server=${this@Server}, feature=$feature, time=$time)." }
        }
      }
      jobs.joinAll()
    }
  }

  override fun toString(): String =
    "Server(name='$name', state='$state')"
}
