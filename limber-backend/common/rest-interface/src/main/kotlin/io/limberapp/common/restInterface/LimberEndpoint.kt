package io.limberapp.common.restInterface

import io.limberapp.rep.ValidatedRep
import io.limberapp.util.url.href
import org.slf4j.LoggerFactory
import java.time.ZoneId
import java.util.*
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor

/**
 * Instances represent parameterized requests to an HTTP endpoint.
 */
@Suppress("UnnecessaryAbstractClass")
abstract class LimberEndpoint(
  val httpMethod: HttpMethod,
  val path: String,
  val queryParams: List<Pair<String, String>> = emptyList(),
  val contentType: ContentType = ContentType.JSON,
  val body: ValidatedRep? = null,
) {
  val href = href(path, queryParams)
}

private val logger = LoggerFactory.getLogger(LimberEndpoint::class.java)

/**
 * This method uses JVM reflection to create a [LimberEndpointTemplate] from a [LimberEndpoint] class reference. There's
 * definitely some magic going on here, so it's reasonably well documented. Temporary "placeholder" arguments are
 * generated in order to instantiate the [LimberEndpoint] using its primary constructor. Those endpoints are then
 * detected by string matching and replaced by Ktor template values according to the names of the [LimberEndpoint]
 * parameters.
 */
@Suppress("NestedBlockDepth")
fun KClass<out LimberEndpoint>.template(): LimberEndpointTemplate {
  @Suppress("TooGenericExceptionCaught")
  try {
    // Trivial case for singleton objects.
    val objectInstance = objectInstance
    if (objectInstance != null) return objectInstance.toTemplate()

    // Find the primary constructor.
    val constructor = checkNotNull(primaryConstructor)

    // Determine what arguments to use to invoke the constructor, and generate temporary placeholder values.
    val (args, argReplacements) = generateArgsAndReplacements(constructor)

    // Instantiate an instance of the endpoint using the temporary placeholder values and the primary constructor.
    val endpoint = constructor.callBy(args)

    // Generate the path template and deteremine query parameter names.
    var pathTemplate = endpoint.path
    val requiredQueryParams = mutableSetOf<String>()
    argReplacements.forEach { (name, value) ->
      // Splitting the path template by the temporary placeholder value counts the number of occurrences.
      val split = pathTemplate.split(value)

      // The number of matches is 1 fewer than the size of the split.
      when (split.size - 1) {
        // This must be a query param. If it's not nullable, add it to the set.
        0 -> if (args.withName(name).let { !it.isOptional && !it.type.isMarkedNullable }) requiredQueryParams.add(name)
        // Perform the path replacement.
        1 -> pathTemplate = split.joinToString("{${name}}")
        // Multiple matches indicate an error.
        else -> error("${endpoint.path} contains more than 1 match of $value.")
      }
    }

    return LimberEndpointTemplate(
      httpMethod = endpoint.httpMethod,
      pathTemplate = pathTemplate,
      requiredQueryParams = requiredQueryParams,
      contentType = endpoint.contentType
    )
  } catch (e: Exception) {
    logger.error("Unable to construct template.", e)
    throw e
  }
}

private fun LimberEndpoint.toTemplate(): LimberEndpointTemplate {
  return LimberEndpointTemplate(
    httpMethod = httpMethod,
    pathTemplate = path,
    requiredQueryParams = queryParams.map { it.first }.toSet(),
    contentType = contentType
  )
}

private fun generateArgsAndReplacements(
  constructor: KFunction<LimberEndpoint>,
): Pair<Map<KParameter, Any?>, MutableMap<String, String>> {
  // The key is the arg name, and the value is the temporary placeholder value.
  val argReplacements = mutableMapOf<String, String>()

  // Determine what arguments to use to invoke the constructor.
  // At the same time, record any generated temporary placeholder values and the corresponding arg names.
  val args = constructor.parameters.associateWith { arg ->
    val argName = checkNotNull(arg.name)

    val kClass = arg.type.classifier as KClass<*>
    return@associateWith when {
      // Generate a random integer.
      kClass == Int::class -> Random.nextInt().also {
        // Integers should also go into arg replacements.
        argReplacements[argName] = it.toString()
      }
      // Generate a random string.
      kClass == String::class -> UUID.randomUUID().toString().also {
        // Strings should also go into arg replacements.
        argReplacements[argName] = it
      }
      // Generate a random time zone.
      kClass == ZoneId::class -> ZoneId.of(ZoneId.getAvailableZoneIds().random()).also {
        // Time zones should also go into arg replacements.
        argReplacements[argName] = it.toString()
      }
      // Generate a random UUID.
      kClass == UUID::class -> UUID.randomUUID().also {
        // UUIDs should also go into arg replacements.
        argReplacements[argName] = it.toString()
      }
      // Just use the first value in the enum.
      // This will cause code below to break if there are multiple args with the same enum type,
      // but we can cross that bridge if and when we come to it.
      kClass.isSubclassOf(Enum::class) -> kClass.java.enumConstants.first().also {
        // Enums should also go into arg replacements.
        argReplacements[argName] = it.toString()
      }
      // Rep args must be nullable for this to work, so we'll just pass null.
      kClass.isSubclassOf(ValidatedRep::class) -> null
      else -> unknownType(arg.type)
    }
  }
  return Pair(args, argReplacements)
}

private fun Map<KParameter, Any?>.withName(name: String): KParameter = entries.single { it.key.name == name }.key
