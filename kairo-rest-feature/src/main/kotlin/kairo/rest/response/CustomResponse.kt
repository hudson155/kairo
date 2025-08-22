package kairo.rest.response

import io.ktor.server.routing.RoutingCall

public abstract class CustomResponse {
  @Suppress("SuspendFunWithCoroutineScopeReceiver")
  public abstract suspend fun RoutingCall.respond()
}
