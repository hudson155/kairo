package kairo.rest

import io.ktor.server.routing.Routing
import kotlin.reflect.KClass

public class RestEndpointHandler<I : Any, O : Any, E : RestEndpoint<I, O>> {
  internal var handle: (suspend (endpoint: E) -> O)? = null

  public fun handle(handle: suspend (endpoint: E) -> O) {
    // TODO: Verify handle is set exactly once.
    this.handle = handle
  }
}

@Suppress("unused") // TODO: Don't do this.
public inline fun <reified I : Any, reified O : Any, reified E : RestEndpoint<I, O>> Routing.route(
  kClass: KClass<E>,
  noinline block: RestEndpointHandler<I, O, E>.() -> Unit,
) {
  TODO()
}
