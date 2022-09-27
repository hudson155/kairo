package limber.rest.endpointTemplate

import io.mockk.mockkClass
import limber.rest.RestEndpoint
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.valueParameters

/**
 * Represents a parameter for a REST endpoint.
 * Used during endpoint template generation (see [RestEndpointTemplateBuilder]).
 */
public sealed class Parameter(public val delegate: KParameter) {
  public val name: String = checkNotNull(delegate.name)

  internal abstract val random: () -> Any

  public class Body(delegate: KParameter) : Parameter(delegate) {
    override val random = { mockkClass(delegate.type.classifier as KClass<*>) }
  }

  public class Path(
    delegate: KParameter,
    override val random: () -> Any,
  ) : Parameter(delegate)

  internal companion object {
    /**
     * Generates a list of [Parameter]s for a [RestEndpoint], given its class reference.
     */
    fun from(constructor: KFunction<RestEndpoint>): List<Parameter> =
      constructor.valueParameters.map { parameter ->
        val kClass = parameter.type.classifier as KClass<*>

        if (parameter.name == RestEndpoint::body.name) {
          return@map Body(parameter)
        }

        @Suppress("UseIfInsteadOfWhen") // More conditions are coming soon.
        return@map when (kClass) {
          UUID::class -> Path(parameter) { UUID.randomUUID() }
          else -> error("${kClass.simpleName} is not a supported endpoint parameter type.")
        }
      }
  }
}
