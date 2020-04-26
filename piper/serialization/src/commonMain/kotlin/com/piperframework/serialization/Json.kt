package com.piperframework.serialization

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.modules.EmptyModule
import kotlinx.serialization.modules.SerialModule
import kotlinx.serialization.modules.getContextualOrDefault
import kotlin.jvm.JvmName
import kotlin.reflect.KClass

/**
 * Class to serialize/deserialize [kotlinx.serialization.Serializable] classes.
 */
@Suppress("UNCHECKED_CAST")
@OptIn(ImplicitReflectionSerializer::class)
class Json(prettyPrint: Boolean = false, context: SerialModule = EmptyModule) {
    /**
     * Most functionality is delegated to this class (which has the same name). It must be public due to the inline
     * functions.
     */
    val json = Json(JsonConfiguration.Stable.copy(prettyPrint = prettyPrint), context = context)

    /**
     * Parse string to arbitrary class.
     * Cleaner interface used when the class is available at compile time.
     */
    inline fun <reified T : Any> parse(string: String): T =
        json.parse(getSerializer(), string)

    /**
     * Parse string to arbitrary class.
     * More verbose interface used when the class is only available at run time.
     */
    fun <T : Any> parse(string: String, kClass: KClass<T>): T =
        json.parse(getSerializer(kClass), string)

    /**
     * Parse string to arbitrary set of classes.
     * Cleaner interface used when the class is available at compile time.
     */
    inline fun <reified T : Any> parseSet(string: String): Set<T> =
        json.parse(SetSerializer(getSerializer()), string)

    /**
     * Parse string to arbitrary list of classes.
     * Cleaner interface used when the class is available at compile time.
     */
    inline fun <reified T : Any> parseList(string: String): List<T> =
        json.parse(ListSerializer(getSerializer()), string)

    /**
     * Stringify arbitrary class to string.
     */
    fun stringify(model: Any): String =
        json.stringify(getSerializer(model::class) as SerializationStrategy<Any>, model)

    /**
     * Stringify arbitrary set of classes to string.
     * Faster implementation used when the class is available at compile time.
     */
    @JvmName("stringifySetDynamically")
    inline fun <reified T : Any> stringifySet(model: Set<T>): String =
        json.stringify(SetSerializer(getSerializer()), model)

    /**
     * Stringify arbitrary list of classes to string.
     * Faster implementation used when the class is available at compile time.
     */
    @JvmName("stringifyListDynamically")
    inline fun <reified T : Any> stringifyList(model: List<T>): String =
        json.stringify(ListSerializer(getSerializer()), model)

    /**
     * Stringify arbitrary set of classes to string.
     * Slower implementation used when the class is available at compile time.
     */
    @JvmName("stringifySetStatically")
    fun stringifySet(model: Set<*>): String {
        val serializer = elementSerializer(model)
        return json.stringify(SetSerializer(serializer), model as Set<Any>)
    }

    /**
     * Stringify arbitrary list of classes to string.
     * Slower implementation used when the class is available at compile time.
     */
    @JvmName("stringifyListStatically")
    fun stringifyList(model: List<*>): String {
        val serializer = elementSerializer(model)
        return json.stringify(ListSerializer(serializer), model as List<Any>)
    }

    private fun elementSerializer(model: Collection<*>): KSerializer<Any> {
        val serializers = model.mapNotNull { it?.let { getSerializer(it::class) } }
        return if (serializers.isEmpty()) {
            String.serializer()
        } else {
            if (serializers.distinct().size > 1) throw UnsupportedOperationException()
            serializers.first()
        } as KSerializer<Any>
    }

    inline fun <reified T : Any> getSerializer(): KSerializer<T> =
        json.context.getContextualOrDefault(T::class)

    private fun <T : Any> getSerializer(kClass: KClass<T>): KSerializer<T> =
        json.context.getContextualOrDefault(kClass)
}
