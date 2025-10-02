package kairo.rest.auth

import io.ktor.server.routing.RoutingCall
import kairo.rest.RestEndpoint

@Suppress("UseDataClass")
public class AuthReceiver<E : RestEndpoint<*, *>> internal constructor(
  public val call: RoutingCall,
  public val endpoint: E,
)

public fun AuthReceiver<*>.public(): Unit =
  Unit
