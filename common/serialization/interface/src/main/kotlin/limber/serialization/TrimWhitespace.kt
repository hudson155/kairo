package limber.serialization

/**
 * String properties have their starting and ending whitespace trimmed.
 * This behaviour can be customized using this annotation.
 */
@Target(AnnotationTarget.FIELD)
public annotation class TrimWhitespace(val type: Type) {
  public enum class Type(public val trimStart: Boolean, public val trimEnd: Boolean) {
    TrimNone(trimStart = false, trimEnd = false),
    TrimStart(trimStart = true, trimEnd = false),
    TrimEnd(trimStart = false, trimEnd = true),
    TrimBoth(trimStart = true, trimEnd = true);
  }
}
