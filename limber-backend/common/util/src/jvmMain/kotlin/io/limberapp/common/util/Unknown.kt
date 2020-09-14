@file:JvmName("UnknownJvmKt")

package io.limberapp.common.util

import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure

fun unknownType(type: KType): Nothing =
  error("Unknown type: ${type.jvmErasure.simpleName}")
