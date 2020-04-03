package com.piperframework.util

import kotlin.reflect.KClass

fun unknown(type: String, klass: KClass<*>): Nothing = error("Unknown $type type: ${klass.simpleName}")
