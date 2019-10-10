package io.limberapp.framework.dataConversion

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.application.ApplicationCall
import io.ktor.features.ContentConverter
import io.ktor.http.ContentType
import io.ktor.jackson.JacksonConverter
import io.ktor.request.ApplicationReceiveRequest
import io.ktor.util.pipeline.PipelineContext
import io.limberapp.framework.error.FrameworkError
import io.limberapp.framework.error.internal.InternalFrameworkError
import io.limberapp.framework.model.Model
import io.limberapp.framework.model.Updater
import kotlin.reflect.KFunction1

/**
 * CustomJacksonConverter acts as a standard Jackson converter to convert entities to JSON. The
 * difference between this custom implementation and what one might consider to be a "standard"
 * implementation is that this implementation ensures that entities are a subclass of the Model
 * interface, both when serializing and deserializing. It also manages creator-unknown properties.
 *
 * This class is adapted from io.ktor.jackson.JacksonConverter.
 */
class CustomJacksonConverter(mapper: ObjectMapper) : ContentConverter {

    private val delegate = JacksonConverter(mapper)

    /**
     * In addition to the default (delegate) conversion, this ensures that the deserialized entity
     * is a Model and nulls
     * out its creator-unknown properties.
     */
    override suspend fun convertForReceive(
        context: PipelineContext<ApplicationReceiveRequest, ApplicationCall>
    ): Any? {
        val result = delegate.convertForReceive(context)
        return convert(result, Model<*>::asCreator)
    }

    /**
     * In addition to the default (delegate) conversion, this ensures that the entity to serialize
     * is a Model and that its creator-unknown properties are not null.
     */
    override suspend fun convertForSend(
        context: PipelineContext<Any, ApplicationCall>,
        contentType: ContentType,
        value: Any
    ): Any? {
        val convertedValue = when (value) {
            is Model<*> -> convert(value, Model<*>::asResult)
            is FrameworkError -> value
            else -> InternalFrameworkError("Unsupported return type: ${value::class.qualifiedName}")
        }
        return delegate.convertForSend(context, contentType, convertedValue)
    }

    private fun convert(
        value: Any?,
        converter: KFunction1<Model<*>, Model<*>>
    ): Any {
        return when (value) {
            is List<*> -> value.map { convert(it, converter) }
            is Model<*> -> {
                value.validate()
                converter(value)
            }
            is Updater<*> -> value
            else -> error("Unknown type: ${value?.let { it::class.qualifiedName }}")
        }
    }
}
