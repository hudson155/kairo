package kairo.rest.response

import io.ktor.server.routing.RoutingCall

public abstract class CustomResponse {
  public abstract suspend fun RoutingCall.respond()
}
