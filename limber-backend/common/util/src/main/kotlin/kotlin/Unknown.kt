package kotlin

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure

fun unknownType(description: String, kClass: KClass<*>): Nothing =
  error("Unknown $description type: ${kClass.simpleName}")

fun unknownType(type: KType): Nothing =
  error("Unknown type: ${type.jvmErasure.simpleName}")
