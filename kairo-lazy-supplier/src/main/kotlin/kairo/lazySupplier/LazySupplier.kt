package kairo.lazySupplier

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

public class LazySupplier<T>(private val fetch: suspend () -> T) {
  private val mutex: Mutex = Mutex()
  private var isInitialized: Boolean = false
  private var value: T? = null

  public suspend fun get(): T =
    mutex.withLock {
      if (!isInitialized) {
        value = fetch()
        isInitialized = true
      }
      @Suppress("UNCHECKED_CAST")
      return@withLock value as T
    }
}
