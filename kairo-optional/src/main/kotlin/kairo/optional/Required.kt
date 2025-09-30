package kairo.optional

/**
 * Corollary to [Optional].
 */
public sealed class Required<out T : Any> : OptionalBase<T>() {
  abstract override fun getOrThrow(): T

  public data object Missing : Required<Nothing>() {
    override val isSpecified: Boolean = false

    override fun getOrThrow(): Nothing {
      error("Required value is missing.")
    }
  }

  public data class Value<T : Any>(val value: T) : Required<T>() {
    override val isSpecified: Boolean = true

    override fun getOrThrow(): T =
      value
  }

  public companion object {
    public fun <T : Any> of(value: T): Required<T> =
      Value(value)
  }
}

public fun <T : Any> Required<T>.ifSpecified(block: (T) -> Unit) {
  when (this) {
    is Required.Missing -> Unit
    is Required.Value -> block(value)
  }
}

public fun <T : Any, R : Any> Required<T>.transform(block: (T) -> R): Required<R> =
  when (this) {
    is Required.Missing -> this
    is Required.Value -> Required.of(block(value))
  }
