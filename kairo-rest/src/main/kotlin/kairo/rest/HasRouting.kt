package kairo.rest

import io.ktor.server.application.Application

/**
 * Use this interface on any Features that wish to add Koin bindings.
 */
public interface HasRouting {
  public fun Application.routing()
}
