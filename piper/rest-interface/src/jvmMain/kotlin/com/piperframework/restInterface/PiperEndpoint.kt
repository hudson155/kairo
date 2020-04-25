package com.piperframework.restInterface

import com.piperframework.rep.ValidatedRep
import com.piperframework.util.unknownType
import org.slf4j.LoggerFactory
import java.util.UUID
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor

private val logger = LoggerFactory.getLogger(PiperEndpoint::class.java)

/**
 * This method uses JVM reflection to create a [PiperEndpointTemplate] from a [PiperEndpoint] class reference. There's
 * definitely some magic going on here, so it's reasonably well documented. Temporary "placeholder" arguments are
 * generated in order to instantiate the [PiperEndpoint] using its primary constructor. Those endpoints are then
 * detected by string matching and replaced by Ktor template values according to the names of the [PiperEndpoint]
 * parameters.
 */
fun KClass<out PiperEndpoint>.template(): PiperEndpointTemplate {
    @Suppress("TooGenericExceptionCaught")
    try {
        // Trivial case for singleton objects.
        val objectInstance = objectInstance
        if (objectInstance != null) {
            return PiperEndpointTemplate(
                httpMethod = objectInstance.httpMethod,
                pathTemplate = objectInstance.path
            )
        }

        // Find the default constructor and ensure we can access it even.
        val constructor = checkNotNull(primaryConstructor)

        // The first of each pair is a temporary placeholder argument, the second is the Ktor template value.
        val argReplacements = mutableListOf<Pair<String, String>>()

        // Map each arg to a temporary placeholder value.
        // At the same time, record those temporary placeholder values and the corresponding Ktor template values.
        val args = constructor.parameters.associateWith {
            // The template value is simply the parameter name surrounded by braces. Ktor expects this.
            val templateValue = "{${it.name}}"

            // Depending on the type of the parameter, generate a temporary placeholder value differently.
            val type = it.type
            val kClass = it.type.classifier as KClass<*>
            val placeholderValue = when {
                type.isMarkedNullable -> null
                kClass == Int::class -> Random.nextInt()
                kClass == String::class -> UUID.randomUUID().toString()
                kClass == UUID::class -> UUID.randomUUID()
                kClass.isSubclassOf(Enum::class) -> kClass.java.enumConstants.first()
                kClass.isSubclassOf(ValidatedRep::class) -> null
                else -> unknownType(it.type)
            }

            // Record the temporary placeholder value and corresponding Ktor template value in argReplacements.
            argReplacements.add(placeholderValue.toString() to templateValue)
            return@associateWith placeholderValue
        }

        // Instantiate an instance of the endpoint using the temporary placeholder values and the primary constructor.
        val endpoint = constructor.callBy(args)

        // Replace each temporary placeholder value with the corresponding Ktor template value.
        val pathTemplate = argReplacements.fold(endpoint.path) { acc, pair ->

            // Splitting the path template by the temporary placeholder value counts the number of occurrences.
            val split = acc.split(pair.first)

            // If and only if there is 1 occurrence, the split size will be 2.
            if (split.size > 2) error("$acc contains more than 1 matches of ${pair.first}.")

            // Joining using the Ktor template value as the delimiter completes the replacement process.
            return@fold split.joinToString(pair.second)
        }

        return PiperEndpointTemplate(
            httpMethod = endpoint.httpMethod,
            pathTemplate = pathTemplate
        )
    } catch (e: Exception) {
        logger.error("Unable to construct template.", e)
        throw e
    }
}
