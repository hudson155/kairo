package kairo.rest

import io.ktor.server.application.Application
import io.ktor.util.AttributeKey
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

private val key: AttributeKey<MutableSet<KClass<out RestEndpoint<*, *>>>> =
  AttributeKey("restEndpointClasses")

public val Application.restEndpointClasses: MutableSet<KClass<out RestEndpoint<*, *>>>
  get() {
    val existing = attributes.getOrNull(key)
    if (existing != null) return existing
    val new: MutableSet<KClass<out RestEndpoint<*, *>>> = ConcurrentHashMap.newKeySet()
    attributes.put(key, new)
    return new
  }
