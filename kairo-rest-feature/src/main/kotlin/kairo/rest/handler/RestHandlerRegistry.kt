package kairo.rest.handler

import kairo.rest.endpoint.RestEndpoint
import kotlin.reflect.KClass

public typealias RestHandlerRegistry = Map<KClass<out RestEndpoint<*, *>>, RestHandler<*, *>>
