package kairo.rest.exceptionHandler

internal inline fun <reified T : Any> Throwable.findCause(): T? {
  var result: Throwable? = this
  while (true) {
    if (result == null) return null
    if (result is T) return result
    result = result.cause
  }
}
