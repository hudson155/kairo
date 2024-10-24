package kairo.util

public fun kairoToString(name: String, properties: List<Pair<String, Any>>): String =
  properties.joinToString(prefix = "$name(", postfix = ")") { property ->
    "${property.first}=${property.second}"
  }
