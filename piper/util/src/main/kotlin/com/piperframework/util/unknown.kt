package com.piperframework.util

import kotlin.reflect.KClass

fun unknown(type: String, clazz: KClass<*>): Nothing = error("Unknown $type type: ${clazz.simpleName}")
