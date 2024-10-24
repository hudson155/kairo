package kairo.util

public fun kairoHashCode(vararg properties: Any?): Int {
  var result = 0
  properties.forEach { property ->
    result = 31 * result + property.hashCode()
  }
  return result
}
