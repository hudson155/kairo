package kairo.rest

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.server.response.header
import io.ktor.server.routing.RoutingContext
import io.ktor.server.sse.SSEServerContent
import io.ktor.server.sse.ServerSSESession
import io.ktor.server.sse.sse

/**
 * Adapted from [sse].
 */
public fun RoutingContext.serverSideEvents(
  handler: suspend ServerSSESession.() -> Unit,
): SSEServerContent {
  call.response.header(HttpHeaders.ContentType, ContentType.Text.EventStream.toString())
  call.response.header(HttpHeaders.CacheControl, "no-store")
  call.response.header(HttpHeaders.Connection, "keep-alive")
  call.response.header("X-Accel-Buffering", "no")
  return SSEServerContent(call, handler)
}
