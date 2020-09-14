@file:JvmName("UnknownCommonKt")

package io.limberapp.common.util

import kotlin.jvm.JvmName
import kotlin.reflect.KClass

fun unknownType(description: String, kClass: KClass<*>): Nothing =
  error("Unknown $description type: ${kClass.simpleName}")

fun unknownValue(description: String, value: Any): Nothing =
  error("Unknown $description: $value")
