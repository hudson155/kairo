package kairo.rest.sse

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import io.ktor.server.sse.SSEServerContent
import io.ktor.server.sse.ServerSSESession
import kairo.reflect.KairoType
import kairo.reflect.kairoType
import kairo.rest.KtorServerMapper
import kairo.rest.response.CustomResponse
import kairo.serialization.util.kairoWriteSpecial
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

public class ServerSideEvents<O : Any>(
  private val flow: Flow<O>,
  private val type: KairoType<O>,
) : CustomResponse() {
  public override suspend fun RoutingCall.respond() {
    response.header(HttpHeaders.ContentType, ContentType.Text.EventStream.toString())
    response.header(HttpHeaders.CacheControl, "no-store")
    response.header(HttpHeaders.Connection, "keep-alive")
    response.header("X-Accel-Buffering", "no")
    val handle: suspend ServerSSESession.() -> Unit = {
      flow.collect { event ->
        send(KtorServerMapper.json.kairoWriteSpecial(event, type))
      }
    }
    respond(SSEServerContent(this, handle, null))
  }
}

public inline fun <reified O : Any> serverSideEvents(
  noinline block: suspend FlowCollector<O>.() -> Unit,
): ServerSideEvents<O> =
  ServerSideEvents(flow(block), kairoType())
