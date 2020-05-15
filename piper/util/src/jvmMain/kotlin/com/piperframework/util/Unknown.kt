@file:JvmName("UnknownJvmKt")

package com.piperframework.util

import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure

fun unknownType(type: KType): Nothing =
  error("Unknown type: ${type.jvmErasure.simpleName}")
