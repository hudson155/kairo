package kairo.protectedString

import kotlinx.serialization.Serializable

/**
 * Secrets should never show up in logs or stack traces.
 * Protected strings are a lightweight wrapper around sensitive strings
 * that improves safety without complicating your code.
 */
@Suppress("UseDataClass")
@OptIn(ProtectedString.Access::class)
@Serializable(with = ProtectedStringSerializer::class)
public class ProtectedString @Access constructor(
  @Access
  public val value: String,
) {
  /**
   * You must opt in to create protected strings or access their values.
   */
  @RequiresOptIn
  @Target(AnnotationTarget.CONSTRUCTOR, AnnotationTarget.PROPERTY)
  public annotation class Access

  override fun equals(other: Any?): Boolean {
    if (other !is ProtectedString) return false
    if (value != other.value) return false
    return true
  }

  override fun hashCode(): Int =
    value.hashCode()

  /**
   * Safe by default: [toString] redacts the value.
   */
  override fun toString(): String =
    "ProtectedString(value='REDACTED')"
}
