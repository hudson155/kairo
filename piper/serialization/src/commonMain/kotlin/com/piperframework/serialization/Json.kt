package com.piperframework.serialization

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.modules.EmptyModule
import kotlinx.serialization.modules.SerialModule
import kotlinx.serialization.modules.getContextualOrDefault
import kotlin.jvm.JvmName
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
@OptIn(ImplicitReflectionSerializer::class)
class Json(prettyPrint: Boolean = false, context: SerialModule = EmptyModule) {

    val json = Json(JsonConfiguration.Stable.copy(prettyPrint = prettyPrint), context = context)

    inline fun <reified T : Any> parse(string: String): T =
        json.parse(getSerializer(), string)

    fun <T : Any> parse(string: String, kClass: KClass<T>): T =
        json.parse(getSerializer(kClass), string)

    inline fun <reified T : Any> parseList(string: String): List<T> =
        json.parse(ListSerializer(getSerializer()), string)

    fun stringify(model: Any): String =
        json.stringify(getSerializer(model::class) as SerializationStrategy<Any>, model)

    @JvmName("stringifyListDynamically")
    inline fun <reified T : Any> stringifyList(model: List<T>): String =
        json.stringify(ListSerializer(getSerializer()), model)

    @JvmName("stringifyListStatically")
    fun stringifyList(model: List<*>): String {
        val serializers = model.mapNotNull { it?.let { getSerializer(it::class) } }
        val serializer = if (serializers.isEmpty()) {
            String.serializer()
        } else {
            if (serializers.distinct().size > 1) throw UnsupportedOperationException()
            serializers.first()
        } as KSerializer<Any>
        return json.stringify(ListSerializer(serializer), model as List<Any>)
    }

    inline fun <reified T : Any> getSerializer(): KSerializer<T> =
        json.context.getContextualOrDefault(T::class)

    private fun <T : Any> getSerializer(kClass: KClass<T>): KSerializer<T> =
        json.context.getContextualOrDefault(kClass)
}
