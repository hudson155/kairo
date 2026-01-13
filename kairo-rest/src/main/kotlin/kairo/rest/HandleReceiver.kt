package kairo.rest

import io.ktor.server.routing.RoutingCall

/**
 * Kotlin receiver for [RestEndpointHandler.handle].
 */
@Suppress("UseDataClass")
public class HandleReceiver<E : RestEndpoint<*, *>> internal constructor(
  public val call: RoutingCall,
  public val endpoint: E,
)
