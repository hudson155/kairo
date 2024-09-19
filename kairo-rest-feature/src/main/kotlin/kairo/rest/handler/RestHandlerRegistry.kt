package kairo.rest.handler

import kairo.rest.endpoint.RestEndpoint
import kotlin.reflect.KClass

internal typealias RestHandlerRegistry = Map<KClass<out RestEndpoint<*, *>>, RestHandler<*, *>>
