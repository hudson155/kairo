package com.piperframework.contentNegotiation

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.features.ContentConverter
import io.ktor.features.suitableCharset
import io.ktor.http.ContentType
import io.ktor.http.content.TextContent
import io.ktor.http.withCharset
import io.ktor.request.ApplicationReceiveRequest
import io.ktor.request.contentCharset
import io.ktor.util.pipeline.PipelineContext
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.getContextualOrDefault

class JsonContentConverter(private val json: Json) : ContentConverter {

    override suspend fun convertForReceive(context: PipelineContext<ApplicationReceiveRequest, ApplicationCall>): Any? {
        val request = context.subject
        val type = request.type
        val value = request.value as? ByteReadChannel ?: return null
        val charset = context.call.request.contentCharset() ?: Charsets.UTF_8
        val reader = value.toInputStream().reader(charset)
        val string = reader.readText()
        val deserializer = json.serializer(type)
        return json.parse(deserializer, string)
    }

    override suspend fun convertForSend(
        context: PipelineContext<Any, ApplicationCall>,
        contentType: ContentType,
        value: Any
    ): Any? {
        val serializer = json.serializer(value)
        val charset = context.call.suitableCharset()
        val string = json.stringify(serializer, value)
        return TextContent(string, contentType.withCharset(charset))
    }

    private fun Json.serializer(value: Any): KSerializer<Any> {
        @Suppress("UseIfInsteadOfWhen")
        val serializer = when (value) {
            is List<*> -> ListSerializer(elementSerializer(value))
            else -> context.getContextualOrDefault(value::class)
        }
        @Suppress("UNCHECKED_CAST")
        return serializer as KSerializer<Any>
    }

    private fun Json.elementSerializer(collection: Collection<*>): KSerializer<*> {

        val serializers = collection.mapNotNull { value -> value?.let { serializer(it) } }
            .distinctBy { it.descriptor.serialName }

        if (serializers.size > 1) {
            error(
                "Serializing collections of different element types is not yet supported." +
                        " Selected serializers: ${serializers.map { it.descriptor.serialName }}"
            )
        }

        val serializer = serializers.singleOrNull() ?: String.serializer()
        if (serializer.descriptor.isNullable) return serializer
        if (collection.any { it == null }) return serializer.nullable
        return serializer
    }
}
