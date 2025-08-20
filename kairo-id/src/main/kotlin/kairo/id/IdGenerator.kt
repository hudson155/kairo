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
      "Invalid ID length (prefix=$prefix, length=${length ?: "null"})." +
        " Must be between 8 and 32 (inclusive)."
    }
  }

  public fun generate(): T {
    val payload = strategy.generate(length)
    val id = "${prefix}_$payload"
    return generate(id)
  }

  protected abstract fun generate(value: String): T
}
