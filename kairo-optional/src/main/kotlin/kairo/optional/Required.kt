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
