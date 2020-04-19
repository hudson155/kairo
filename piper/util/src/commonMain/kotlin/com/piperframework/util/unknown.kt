package com.piperframework.util

import kotlin.reflect.KClass

fun unknown(type: String, kClass: KClass<*>): Nothing = error("Unknown $type type: ${kClass.simpleName}")
