package kairo.rest.util

import io.ktor.util.reflect.TypeInfo
import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure

internal fun typeInfo(type: KType): TypeInfo =
  TypeInfo(type.jvmErasure, type)
