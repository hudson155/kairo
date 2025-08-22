package kairo.rest.response

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
import kairo.serialization.util.kairoWriteSpecial
import kotlinx.coroutines.flow.Flow

public class ServerSideEvents<O : Any>(
  private val flow: Flow<O>,
  private val type: KairoType<O>,
) : CustomResponse() {
  @Suppress("SuspendFunWithCoroutineScopeReceiver")
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

public inline fun <reified O : Any> serverSideEvents(flow: Flow<O>): ServerSideEvents<O> =
  ServerSideEvents(flow, kairoType())
