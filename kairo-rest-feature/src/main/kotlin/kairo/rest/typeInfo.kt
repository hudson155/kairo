package kairo.rest

import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.platformType
import io.ktor.util.reflect.typeInfoImpl
import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure

internal fun typeInfo(type: KType): TypeInfo =
  typeInfoImpl(
    reifiedType = type.platformType,
    kClass = type.jvmErasure,
    kType = type,
  )
