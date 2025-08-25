package kairo.dependencyInjection

import org.koin.core.KoinApplication

/**
 * Use this interface on any Features that wish to bind Koin dependencies for injection.
 */
public interface HasBindings {
  public fun KoinApplication.binding()
}
