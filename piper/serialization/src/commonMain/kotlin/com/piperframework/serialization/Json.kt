package com.piperframework.serialization

import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializerOrNull
import kotlinx.serialization.stringify

inline fun <reified T : Any> Json.stringify(value: T): String {
//    return stringify(value)
    // Kotlinx's Stringify does not support polymorphic serialization.
    val klass = T::class
    val serializer = context.getContextual(klass) ?: klass.serializerOrNull() ?: PolymorphicSerializer(klass)
    return this.stringify(serializer, value)
}
