package kairo.dependencyInjection

import org.koin.core.module.Module

/**
 * Use this interface on any Features that wish to bind Koin dependencies for injection.
 */
public interface KoinModule {
  public fun Module.koin()
}
