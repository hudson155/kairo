package kairo.dependencyInjection

import com.google.inject.Provider
import com.google.inject.Singleton
import kotlin.reflect.full.hasAnnotation

/**
 * Leverages Kotlin's [Lazy] to implement a provider that is lazily initialized.
 * This provider must always have the [Singleton] annotation
 *
 * This should be used with care,
 * since its use can lead to deferring expensive operations beyond Server startup.
 */
public abstract class LazySingletonProvider<T : Any> : Provider<T> {
  init {
    require(this::class.hasAnnotation<Singleton>()) {
      "Instances of ${LazySingletonProvider::class.simpleName!!} require @${Singleton::class.simpleName!!}."
    }
  }

  private val value: Lazy<T> = lazy { create() }

  final override fun get(): T =
    value.value

  protected abstract fun create(): T
}
