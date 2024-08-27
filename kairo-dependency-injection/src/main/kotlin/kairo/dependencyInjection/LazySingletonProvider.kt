package kairo.dependencyInjection

import com.google.inject.Provider

/**
 * Leverages Kotlin's [Lazy] to implement a singleton provider that is lazily initialized.
 *
 * This should be used with care,
 * since its use can lead to deferring expensive operations beyond Server startup.
 */
public abstract class LazySingletonProvider<T : Any> : Provider<T> {
  private val value: Lazy<T> = lazy(::create)

  final override fun get(): T =
    value.value

  protected abstract fun create(): T
}
