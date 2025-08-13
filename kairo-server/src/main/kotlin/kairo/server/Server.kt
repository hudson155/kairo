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
 * A Kairo Server combines [Feature]s with arbitrary functionality, running them all together.
 */
public class Server(
  private val name: String,
  private val features: List<Feature>,
) {
  private val lock: Mutex = Mutex()

  @Volatile
  public var state: ServerState = ServerState.Default
    private set

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
   * Distributes [LifecycleEvent.Start] to all Features.
   * This happens concurrently, within a [CoroutineScope].
   */
  private suspend fun onStart() {
    coroutineScope {
      val jobs = features.map { feature ->
        launch(Dispatchers.IO) {
          logger.info { "Starting Feature (server=${this@Server}, feature=$feature)." }
          val time = measureTime {
            try {
              feature.on(LifecycleEvent.Start)
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
  }

  /**
   * Distributes [LifecycleEvent.Stop] to all Features.
   * This happens concurrently, within a [CoroutineScope].
   */
  private suspend fun onStop() {
    supervisorScope {
      val jobs = features.map { feature ->
        launch(Dispatchers.IO) {
          logger.info { "Stopping Feature (server=${this@Server}, feature=$feature)." }
          val time = measureTime {
            try {
              feature.on(LifecycleEvent.Stop)
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
    "Server(name='$name', state=$state)"
}
