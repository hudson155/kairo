package kairo.util

import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

public fun <T : Any> kairoToString(obj: T, kClass: KClass<T>): String =
  kairoToString(
    name = kClass.simpleName!!,
    properties = kClass.memberProperties.map { property ->
      Pair(property.name, property.get(obj))
    }
  )

public fun kairoToString(name: String, properties: List<Pair<String, Any?>>): String =
  properties.joinToString(prefix = "$name(", postfix = ")") { property ->
    "${property.first}=${property.second?.toString() ?: "null"}"
  }
