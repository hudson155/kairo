package kairo.id

/**
 * An ID generator is always tied to a specific prefix.
 */
public abstract class IdGenerator<T : Any>(
  private val strategy: IdGenerationStrategy,
  private val prefix: String,
  private val length: Int? = null,
) {
  init {
    require(length == null || length in 8..32) {
      "Invalid ID length (prefix=$prefix, length=$length)." +
        " Must be between 8 and 32 (inclusive)."
    }
  }

  public fun generate(): T {
    val value = strategy.generate(length)
    val id = Id.of(prefix, value)
    return wrap(id)
  }

  protected abstract fun wrap(id: Id): T
}
