@file:JvmName("UnknownCommonKt")

package com.piperframework.util

import kotlin.jvm.JvmName
import kotlin.reflect.KClass

fun unknownType(description: String, klass: KClass<*>): Nothing =
    error("Unknown $description type: ${klass.simpleName}")

fun unknownValue(description: String, value: Any): Nothing =
    error("Unknown $description: $value")
