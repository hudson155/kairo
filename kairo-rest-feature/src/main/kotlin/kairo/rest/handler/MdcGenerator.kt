package kairo.rest.handler

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.routing.RoutingCall
import kairo.rest.endpoint.RestEndpoint
import kairo.rest.printer.PrettyRestEndpointPrinter
import kairo.rest.template.RestEndpointPath
import kairo.rest.template.RestEndpointTemplate
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

private val logger: KLogger = KotlinLogging.logger {}

internal class MdcGenerator(
  @Suppress("unused") private val call: RoutingCall,
  private val template: RestEndpointTemplate,
  private val endpoint: RestEndpoint<*, *>,
) {
  fun generate(): Map<String, Any?> =
    restMdc() + endpointMdc()

  private fun restMdc(): Map<String, Any?> =
    mapOf(
      "ktor.rest.method" to PrettyRestEndpointPrinter.writeMethod(template),
      "ktor.rest.path" to PrettyRestEndpointPrinter.writePath(template),
      "ktor.rest.query" to PrettyRestEndpointPrinter.writeQuery(template),
      "ktor.rest.contentType" to PrettyRestEndpointPrinter.writeContentType(template),
      "ktor.rest.accept" to PrettyRestEndpointPrinter.writeAccept(template),
    )

  @Suppress("UNCHECKED_CAST")
  private fun endpointMdc(): Map<String, Any?> {
    val properties = endpoint::class.declaredMemberProperties as Collection<KProperty1<RestEndpoint<*, *>, *>>
    val names = template.path.components.filterIsInstance<RestEndpointPath.Component.Param>()
      .map { it.value } + template.query.params.map { it.value }
    logger.debug { "Names are $names." }
    return names.associate { name ->
      val property = properties.single { it.name == name }
      return@associate Pair("ktor.rest.param.$name", property.get(endpoint))
    }
  }
}
