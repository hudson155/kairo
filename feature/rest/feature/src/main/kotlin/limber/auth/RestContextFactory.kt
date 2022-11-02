package limber.auth

import io.ktor.server.application.ApplicationCall

internal fun interface RestContextFactory {
  fun create(call: ApplicationCall): RestContext
}
