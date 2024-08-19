package kairo.id

public abstract class KairoIdGenerator(protected val prefix: String) {
  public abstract class Factory {
    public abstract fun withPrefix(prefix: String): KairoIdGenerator
  }

  public abstract fun generate(): KairoId
}
