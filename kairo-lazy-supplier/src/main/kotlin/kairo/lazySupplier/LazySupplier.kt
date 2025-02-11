package kairo.lazySupplier

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private val logger: KLogger = KotlinLogging.logger {}

public class LazySupplier<T>(private val fetch: suspend () -> T) {
  private val mutex: Mutex = Mutex()
  private var isInitialized: Boolean = false
  private var value: T? = null

  public suspend fun get(): T =
    mutex.withLock {
      if (!isInitialized) {
        logger.debug { "Fetching." }
        value = fetch()
        isInitialized = true
      }
      @Suppress("UNCHECKED_CAST")
      return@withLock value as T
    }
}
