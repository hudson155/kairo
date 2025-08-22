package kairo.rest.writer

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.Parameters
import io.ktor.http.formUrlEncode
import java.util.concurrent.ConcurrentHashMap
import kairo.rest.KtorServerMapper
import kairo.rest.endpoint.RestEndpoint
import kairo.rest.endpoint.RestEndpointDetails
import kairo.rest.template.RestEndpointPath
import kairo.rest.template.RestEndpointTemplate
import kairo.serialization.util.kairoWriteSpecial
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

private val logger: KLogger = KotlinLogging.logger {}

/**
 * [RestEndpointWriter] is responsible for writing the path for an instance of [E].
 */
public class RestEndpointWriter<in E : RestEndpoint<*, *>>(
  endpointKClass: KClass<E>,
) {
  private val template: RestEndpointTemplate =
    RestEndpointTemplate.from(endpointKClass)

  @Suppress("UNCHECKED_CAST")
  private val properties: Collection<KProperty1<RestEndpoint<*, *>, *>> =
    endpointKClass.declaredMemberProperties as Collection<KProperty1<RestEndpoint<*, *>, *>>

  public fun write(endpoint: E): RestEndpointDetails =
    RestEndpointDetails(
      method = template.method,
      path = path(endpoint) + query(endpoint),
      contentType = template.contentType,
      accept = template.accept,
      body = endpoint.body,
    )

  private fun path(endpoint: E): String {
    val path = template.path.components
    return path.joinToString("/", prefix = "/") { component ->
      when (component) {
        is RestEndpointPath.Component.Constant -> component.value
        is RestEndpointPath.Component.Param -> property(endpoint, name = component.value)!!
      }
    }
  }

  private fun query(endpoint: E): String {
    val query = template.query.params
    if (query.isEmpty()) return ""
    val params = Parameters.build {
      query.forEach { param ->
        property(endpoint, name = param.value)?.let { append(param.value, it) }
      }
    }
    return "?${params.formUrlEncode()}"
  }

  private fun property(endpoint: E, name: String): String? {
    val property = properties.single { it.name == name }
    return property.get(endpoint)?.let { KtorServerMapper.json.kairoWriteSpecial(it) }
  }

  public companion object {
    private val cache: MutableMap<KClass<out RestEndpoint<*, *>>, RestEndpointWriter<*>> = ConcurrentHashMap()

    @Suppress("UNCHECKED_CAST")
    public fun <E : RestEndpoint<*, *>> from(endpointKClass: KClass<out E>): RestEndpointWriter<E> =
      cache.computeIfAbsent(endpointKClass) {
        build(endpointKClass)
      } as RestEndpointWriter<E>

    private fun <E : RestEndpoint<*, *>> build(endpointKClass: KClass<E>): RestEndpointWriter<E> {
      logger.debug { "Building REST endpoint writer for endpoint $endpointKClass." }
      require(endpointKClass.isData) {
        "REST endpoint ${endpointKClass.qualifiedName!!} must be a data class or data object."
      }
      return RestEndpointWriter(endpointKClass)
    }
  }
}
