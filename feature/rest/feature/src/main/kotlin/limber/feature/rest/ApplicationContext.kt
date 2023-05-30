package limber.feature.rest

import com.google.inject.Injector
import io.ktor.server.application.Application
import io.ktor.util.AttributeKey
import limber.config.rest.RestConfig

private val applicationContextKey =
  AttributeKey<ApplicationContext>(
    name = "limber.ktor.attribute.applicationContext",
  )

internal var Application.applicationContext: ApplicationContext
  get() = attributes[applicationContextKey]
  set(value) = attributes.put(applicationContextKey, value)

internal data class ApplicationContext(val config: RestConfig, private val injector: Injector) {
  val endpointHandlers: List<RestEndpointHandler<*, *>> =
    injector.filterBindings()
}

private inline fun <reified T : Any> Injector.filterBindings(): List<T> =
  bindings.mapNotNull { (key, binding) ->
    if (!T::class.java.isAssignableFrom(key.typeLiteral.rawType)) return@mapNotNull null
    return@mapNotNull binding.provider.get() as T
  }
