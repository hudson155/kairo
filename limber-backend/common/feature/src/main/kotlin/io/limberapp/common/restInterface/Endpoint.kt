package io.limberapp.common.restInterface

import io.limberapp.common.rep.ValidatedRep
import io.limberapp.common.util.url.enc
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.ZoneId
import java.util.UUID
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor

private val logger: Logger = LoggerFactory.getLogger(Endpoint::class.java)

/**
 * This method uses JVM reflection to create a [EndpointTemplate] from a [Endpoint] class reference.
 * There's definitely some magic going on here, so it's reasonably well documented. Temporary
 * "placeholder" arguments are generated in order to instantiate the [Endpoint] using its primary
 * constructor. Those placeholders are then detected using string matching, and are replaced by Ktor
 * template values according to the names of the [Endpoint] parameters.
 */
@Suppress("NestedBlockDepth")
fun <E : Endpoint> KClass<E>.template(): EndpointTemplate<E> {
  @Suppress("TooGenericExceptionCaught")
  try {
    // Trivial case for singleton objects.
    objectInstance?.let { return@template it.toTemplate() }

    // Find the primary constructor.
    val constructor = checkNotNull(primaryConstructor)

    // Determine the arguments to use to invoke the constructor, and generate temporary placeholder
    // values for those arguments.
    val (args, argReplacements) = generateArgsAndReplacements(constructor)

    // Instantiate an instance of the endpoint by calling the primary constructor using the
    // temporary placeholder values.
    val endpoint = constructor.callBy(args)

    // Generate the path template and determine query parameter names.
    var pathTemplate = endpoint.path
    val requiredQueryParams = mutableSetOf<String>()
    argReplacements.forEach { (name, value) ->
      // Splitting the path template by the temporary placeholder value counts the number of
      // occurrences of the temporary placeholder value.
      val split = pathTemplate.split(value)

      // The number of matches is 1 fewer than the size of the split.
      when (split.size - 1) {
        // This must be a query param. If it's not nullable, consider it required.
        0 -> if (args.findByName(name).let { !it.isOptional && !it.type.isMarkedNullable }) {
          requiredQueryParams.add(name)
        }
        // This must be a path param. Perform path replacement.
        1 -> pathTemplate = split.joinToString("{${name}}")
        // Multiple matches indicate an error. This can happen randomly (although that should be
        // very infrequent) due to the nature of random temporary placeholder value generation. But
        // if it happens even somewhat often, there's likely a bug.
        else -> error("${endpoint.path} contains more than 1 match of $value.")
      }
    }

    return EndpointTemplate(
        httpMethod = endpoint.httpMethod,
        pathTemplate = pathTemplate,
        requiredQueryParams = requiredQueryParams,
        contentType = endpoint.contentType,
    )
  } catch (e: Exception) {
    logger.error("Unable to construct template.", e)
    throw e
  }
}

private fun <E : Endpoint> E.toTemplate(): EndpointTemplate<E> {
  return EndpointTemplate(
      httpMethod = httpMethod,
      pathTemplate = path,
      requiredQueryParams = queryParams.map { it.first }.toSet(),
      contentType = contentType,
  )
}

private fun generateArgsAndReplacements(
    constructor: KFunction<Endpoint>,
): Pair<Map<KParameter, Any?>, MutableMap<String, String>> {
  // The key is the arg name, and the value is the temporary placeholder value.
  val argReplacements = mutableMapOf<String, String>()

  // Determine the arguments to use to invoke the constructor. At the same time, record any
  // generated temporary placeholder values and the corresponding arg names.
  val args = constructor.parameters.associateWith { arg ->
    val argName = checkNotNull(arg.name)

    val kClass = arg.type.classifier as KClass<*>
    return@associateWith when {
      // Generate a random integer.
      kClass == Int::class -> Random.nextInt().also {
        // Integers should go into arg replacements.
        argReplacements[argName] = enc(it.toString())
      }
      // Generate a random string.
      kClass == String::class -> UUID.randomUUID().toString().also {
        // Strings should go into arg replacements.
        argReplacements[argName] = enc(it)
      }
      // Generate a random time zone.
      kClass == ZoneId::class -> ZoneId.of(ZoneId.getAvailableZoneIds().random()).also {
        // Time zones should go into arg replacements.
        argReplacements[argName] = enc(it.toString())
      }
      // Generate a random UUID.
      kClass == UUID::class -> UUID.randomUUID().also {
        // UUIDs should go into arg replacements.
        argReplacements[argName] = enc(it.toString())
      }
      // Just use the first value in the enum. This will cause code below to break if there are
      // multiple args with the same enum type, but we can cross that bridge if and when we come to
      // it.
      kClass.isSubclassOf(Enum::class) -> kClass.java.enumConstants.first().also {
        // Enums should go into arg replacements.
        argReplacements[argName] = enc(it.toString())
      }
      // Rep args must be nullable for this to work, so we'll just pass null. If this causes an NPE,
      // don't update this code, make the rep arg nullable!
      kClass.isSubclassOf(ValidatedRep::class) -> null
      else -> unknownType(Any::class, kClass)
    }
  }
  return Pair(args, argReplacements)
}

private fun Map<KParameter, Any?>.findByName(name: String): KParameter =
    entries.single { it.key.name == name }.key
