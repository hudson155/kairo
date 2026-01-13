package kairo.rest

import io.ktor.server.routing.RoutingCall

/**
 * Kotlin receiver for [RestEndpointHandler.statusCode].
 */
@Suppress("UseDataClass")
public class StatusCodeReceiver<O : Any> internal constructor(
  public val call: RoutingCall,
  public val response: O,
)
