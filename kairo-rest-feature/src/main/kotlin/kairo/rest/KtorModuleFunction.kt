package kairo.rest

import io.ktor.server.application.Application

public fun interface KtorModuleFunction {
  public fun Application.module()
}
