package kairo.dependencyInjection

import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.Provider

/**
 * A simple provider that uses a delegate function to construct instances from [Injector].
 */
public class DelegatingProvider<T : Any>(
  private val delegate: (injector: Injector) -> T,
) : Provider<T> {
  @Inject
  private lateinit var injector: Injector

  override fun get(): T =
    delegate(injector)
}
