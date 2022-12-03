package limber.config.deserializer

internal data class ConfigResult<out T : Any>(val value: T?) {
  fun <R : Any> map(transform: (value: T?) -> R?): ConfigResult<R> = ConfigResult(transform(value))
}
