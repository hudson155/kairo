package kairo.rest

import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.platformType
import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure

internal fun typeInfo(type: KType): TypeInfo =
  TypeInfo(type.jvmErasure, type.platformType, type)
