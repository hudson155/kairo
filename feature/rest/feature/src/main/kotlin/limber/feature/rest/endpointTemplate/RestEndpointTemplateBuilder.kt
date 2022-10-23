package limber.feature.rest.endpointTemplate

import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.plugin
import io.ktor.server.plugins.dataconversion.DataConversion
import io.ktor.server.request.receive
import io.ktor.util.reflect.TypeInfo
import limber.feature.rest.RestEndpoint
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.jvmErasure

/**
 * Uses JVM reflection to derive the necessary information to create [RestEndpointTemplate]s.
 * The implementations differ for singleton objects and for data classes,
 * but they do follow similar patterns.
 */
internal sealed class RestEndpointTemplateBuilder<E : RestEndpoint> {
  internal abstract val argReplacements: Map<String, String>

  internal abstract val templateInstance: E

  /**
   * This method should determine the parameters from an [ApplicationCall].
   * It runs for each REST request.
   */
  abstract suspend fun parameters(call: ApplicationCall): Map<Parameter, Any?>

  /**
   * This method should create a non-template instance of [RestEndpoint] from the parameters.
   * It runs for each REST request.
   */
  abstract suspend fun endpoint(parameters: Map<Parameter, Any?>): E

  /**
   * The singleton object implementation is almost trivial (at least, comparatively).
   * Due to the singleton nature, the [KClass.objectInstance] will be defined and can be used.
   */
  internal class ObjectInstance<E : RestEndpoint>(kClass: KClass<E>) : RestEndpointTemplateBuilder<E>() {
    override val argReplacements: Map<String, String> = emptyMap()

    override val templateInstance = checkNotNull(kClass.objectInstance)

    override suspend fun parameters(call: ApplicationCall): Map<Parameter, Any?> = emptyMap()

    override suspend fun endpoint(parameters: Map<Parameter, Any?>): E = templateInstance
  }

  /**
   * The data class implementation is complex.
   * It generates random argument values and uses them to construct an instance of [RestEndpoint].
   * The resulting path is then manipulated; the randomly generated values are replaced by their argument names.
   */
  internal class DataClass<E : RestEndpoint>(kClass: KClass<E>) : RestEndpointTemplateBuilder<E>() {
    private val constructor: KFunction<E> = checkNotNull(kClass.primaryConstructor)
    private val parameters: List<Parameter> = Parameter.from(constructor)

    private val args: Map<Parameter, Any>
    override val argReplacements: Map<String, String>

    override val templateInstance: E

    init {
      val (args, argReplacements) = generateArgsAndReplacements()
      this.args = args
      this.argReplacements = argReplacements
      this.templateInstance = constructor.callBy(args.entries.associate { Pair(it.key.delegate, it.value) })
    }

    private fun generateArgsAndReplacements(): Pair<Map<Parameter, Any>, Map<String, String>> {
      val argReplacements = mutableMapOf<String, String>()
      val args = parameters.associateWith { parameter ->
        val value = parameter.random()
        if (parameter is Parameter.Path) argReplacements[parameter.name] = value.toString()
        return@associateWith value
      }
      return Pair(args, argReplacements)
    }

    override suspend fun parameters(call: ApplicationCall): Map<Parameter, Any?> {
      val conversionService = call.application.plugin(DataConversion)

      return parameters.associate { parameter ->
        val value = when (parameter) {
          is Parameter.Body ->
            call.receive(parameter.delegate.type.jvmErasure)
          is Parameter.Path ->
            call.parameters.getAll(parameter.name)?.let { parameters ->
              val typeInfo = TypeInfo(
                type = parameter.delegate.type.classifier as KClass<*>,
                reifiedType = parameter.delegate.type.javaType,
                kotlinType = parameter.delegate.type,
              )
              return@let conversionService.fromValues(parameters, typeInfo)
            }
        }
        return@associate Pair(parameter, value)
      }
    }

    override suspend fun endpoint(parameters: Map<Parameter, Any?>): E =
      constructor.callBy(parameters.mapKeys { (key, _) -> key.delegate })
  }

  internal companion object {
    fun <E : RestEndpoint> from(kClass: KClass<E>): RestEndpointTemplateBuilder<E> =
      when {
        kClass.objectInstance !== null -> ObjectInstance(kClass)
        kClass.isData -> DataClass(kClass)
        else -> @Suppress("NullableToStringCall") error("Unsupported REST endpoint class: ${kClass.simpleName}.")
      }
  }
}
