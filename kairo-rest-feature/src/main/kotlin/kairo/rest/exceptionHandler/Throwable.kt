package kairo.rest.exceptionHandler

internal inline fun <reified T : Any> Throwable.findCause(): T? {
  var result: Throwable? = this
  while (true) {
    if (result == null) return null
    if (result is T) return result
    result = result.cause
  }
}

internal inline fun <reified T : Any> Throwable.rootCause(): T? {
  var result: Throwable = this
  while (true) {
    result.cause?.let { result = it } ?: break
  }
  return result as? T
}

internal inline fun <reified T : Any> Throwable.directCause(): T? =
  cause as? T
