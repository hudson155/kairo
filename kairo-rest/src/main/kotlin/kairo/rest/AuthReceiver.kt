package kairo.rest

import io.ktor.server.routing.RoutingCall

@Suppress("UseDataClass")
public class AuthReceiver<E : RestEndpoint<*, *>> internal constructor(
  public val call: RoutingCall,
  public val endpoint: E,
)
