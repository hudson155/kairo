package kairo.serialization.module.primitives

/**
 * Removes whitespace from the start and/or end of strings during deserialization.
 */
@Target(AnnotationTarget.FIELD)
public annotation class TrimWhitespace(val type: Type) {
  public enum class Type(
    public val trimStart: Boolean,
    public val trimEnd: Boolean,
  ) {
    TrimNone(trimStart = false, trimEnd = false),
    TrimStart(trimStart = true, trimEnd = false),
    TrimEnd(trimStart = false, trimEnd = true),
    TrimBoth(trimStart = true, trimEnd = true),
    ;

    public fun transform(original: String): String {
      var result = original
      if (trimStart) result = result.trimStart()
      if (trimEnd) result = result.trimEnd()
      return result
    }
  }
}
