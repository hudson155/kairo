package kairo.util

import kotlin.reflect.KProperty1

@Suppress("UNCHECKED_CAST")
public fun <T : Any> T.kairoEquals(other: Any?, properties: List<KProperty1<T, *>>): Boolean {
  if (this === other) return true
  if (javaClass != other?.javaClass) return false

  other as T

  properties.forEach { property ->
    if (property.get(this) != property.get(other)) return@kairoEquals false
  }
  return true
}
