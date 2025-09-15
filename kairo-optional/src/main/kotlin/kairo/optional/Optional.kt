package kairo.optional

/**
 * Kairo Optionals can be used to differentiate between missing and null values.
 * This comes in especially handy for RFC 7396 (JSON Merge Patch).
 */
public sealed interface Optional<out T> {
  public data object Missing : Optional<Nothing>

  public data object Null : Optional<Nothing>

  public data class Value<T>(val value: T) : Optional<T>

  public companion object {
    public fun <T : Any> fromNullable(value: T?): Optional<T> =
      if (value == null) Null else Value(value)
  }
}
