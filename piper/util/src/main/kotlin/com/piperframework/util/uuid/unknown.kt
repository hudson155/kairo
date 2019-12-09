package com.piperframework.util.uuid

import kotlin.reflect.KClass

inline fun unknown(type: String, clazz: KClass<*>): Nothing = error("Unknown $type type: ${clazz.simpleName}")
