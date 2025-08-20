package kairo.id

/**
 * An ID generation strategy is not tied to any specific prefix.
 */
public abstract class IdGenerationStrategy {
  public abstract fun generate(length: Int?): String
}
