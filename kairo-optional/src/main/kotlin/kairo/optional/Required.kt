package kairo.optional

/**
 * Corollary to [Optional].
 */
public sealed interface Required<out T : Any> {
  public data object Missing : Required<Nothing>

  public data class Value<T : Any>(val value: T) : Required<T>

  public companion object {
    public fun <T : Any> of(value: T): Required<T> =
      Value(value)
  }
}

public val Required<*>.isSpecified: Boolean
  get() = when (this) {
    is Required.Missing -> false
    is Required.Value -> true
  }

public inline fun <T : Any> Required<T>.ifSpecified(crossinline block: (T) -> Unit) {
  when (this) {
    is Required.Missing -> Unit
    is Required.Value -> block(value)
  }
}

public fun <T : Any> Required<T>.getOrThrow(): T =
  when (this) {
    is Required.Missing -> error("Required value is missing.")
    is Required.Value -> value
  }
