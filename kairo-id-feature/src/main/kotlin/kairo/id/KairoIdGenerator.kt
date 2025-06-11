package kairo.id

public abstract class KairoIdGenerator(
  protected val prefix: String,
  protected val length: Int,
) {
  public abstract class Factory {
    public abstract fun withPrefix(prefix: String): KairoIdGenerator
  }

  init {
    require(length in KairoId.length) {
      "Kairo ID lengths must be between ${KairoId.length.first} and ${KairoId.length.last} (inclusive)."
    }
  }

  public abstract fun generate(): KairoId
}
