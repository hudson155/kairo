package kairo.id

public abstract class KairoIdGenerator(
  protected val prefix: String,
  protected val length: Int,
) {
  public abstract class Factory {
    public abstract fun withPrefix(prefix: String): KairoIdGenerator
  }

  init {
    require(length in kairoIdLength) { "Kairo ID lengths must be between 8 and 127 (inclusive)." }
  }

  public abstract fun generate(): KairoId
}
