package kairo.rest

import kotlin.reflect.KClass

internal typealias RestHandlerRegistry = Map<KClass<out RestEndpoint<*, *>>, RestHandler<*, *>>
