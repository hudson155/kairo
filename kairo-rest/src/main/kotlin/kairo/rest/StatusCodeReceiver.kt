package kairo.rest

import io.ktor.server.routing.RoutingCall

@Suppress("UseDataClass")
public class StatusCodeReceiver<O : Any> internal constructor(
  public val call: RoutingCall,
  public val response: O,
)
