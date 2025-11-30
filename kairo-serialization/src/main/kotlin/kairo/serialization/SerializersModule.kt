package kairo.serialization

import kairo.reflect.KairoType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

@Suppress("UNCHECKED_CAST")
public fun <T : Any> SerializersModule.serializer(type: KairoType<T>): KSerializer<T> =
  serializer(type.kotlinType) as KSerializer<T>
