package kairo.optional

/**
 * Kairo Optionals can be used to differentiate between missing and null values.
 * This comes in especially handy for RFC 7396 (JSON Merge Patch).
 */
public sealed class Optional<out T : Any> : OptionalBase<T>() {
  public data object Missing : Optional<Nothing>() {
    override val isSpecified: Boolean = false

    override fun getOrThrow(): Nothing {
      error("Optional value is missing.")
    }
  }

  public data object Null : Optional<Nothing>() {
    override val isSpecified: Boolean = true

    override fun getOrThrow(): Nothing? =
      null
  }

  public data class Value<T : Any>(val value: T) : Optional<T>() {
    override val isSpecified: Boolean = true

    override fun getOrThrow(): T =
      value
  }

  public companion object {
    public fun <T : Any> fromNullable(value: T?): Optional<T> =
      if (value == null) Null else Value(value)
  }
}

public fun <T : Any> Optional<T>.ifSpecified(block: (T?) -> Unit) {
  when (this) {
    is Optional.Missing -> Unit
    is Optional.Null -> block(null)
    is Optional.Value -> block(value)
  }
}

public fun <T : Any, R : Any> Optional<T>.transform(block: (T?) -> R?): Optional<R> =
  when (this) {
    is Optional.Missing -> this
    is Optional.Null -> Optional.fromNullable(block(null))
    is Optional.Value -> Optional.fromNullable(block(value))
  }
