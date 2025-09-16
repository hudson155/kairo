package kairo.optional

/**
 * Kairo Optionals can be used to differentiate between missing and null values.
 * This comes in especially handy for RFC 7396 (JSON Merge Patch).
 */
public sealed interface Optional<out T : Any> {
  public data object Missing : Optional<Nothing>

  public data object Null : Optional<Nothing>

  public data class Value<T : Any>(val value: T) : Optional<T>

  public companion object {
    public fun <T : Any> fromNullable(value: T?): Optional<T> =
      if (value == null) Null else Value(value)
  }
}

public val Optional<*>.isSpecified: Boolean
  get() = when (this) {
    is Optional.Missing -> false
    is Optional.Null -> true
    is Optional.Value -> true
  }

public fun <T : Any> Optional<T>.ifSpecified(block: (T?) -> Unit) {
  when (this) {
    is Optional.Missing -> Unit
    is Optional.Null -> block(null)
    is Optional.Value -> block(value)
  }
}

public fun <T : Any> Optional<T>.getOrThrow(): T? =
  when (this) {
    is Optional.Missing -> error("Optional value is missing.")
    is Optional.Null -> null
    is Optional.Value -> value
  }
