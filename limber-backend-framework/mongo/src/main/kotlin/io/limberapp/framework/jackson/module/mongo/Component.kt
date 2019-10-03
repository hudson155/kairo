package io.limberapp.framework.jackson.module.mongo

import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import kotlin.reflect.KClass

internal abstract class Component<T : Any>(val clazz: KClass<T>) {
    abstract val serializer: JsonSerializer<T>
    abstract val deserializer: JsonDeserializer<T>
}
