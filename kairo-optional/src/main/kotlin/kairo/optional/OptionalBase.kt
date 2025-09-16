package kairo.optional

public abstract class OptionalBase<out T : Any> {
  public abstract val isSpecified: Boolean

  public abstract fun getOrThrow(): T?
}
