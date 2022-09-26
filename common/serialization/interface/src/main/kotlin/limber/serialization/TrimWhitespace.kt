package limber.serialization

public annotation class TrimWhitespace(val type: Type) {
  public enum class Type(public val trimStart: Boolean, public val trimEnd: Boolean) {
    TrimNone(trimStart = false, trimEnd = false),
    TrimStart(trimStart = true, trimEnd = false),
    TrimEnd(trimStart = false, trimEnd = true),
    TrimBoth(trimStart = true, trimEnd = true);
  }
}
