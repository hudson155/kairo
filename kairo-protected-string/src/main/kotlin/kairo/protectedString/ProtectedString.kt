package kairo.protectedString

import kotlinx.serialization.Serializable

/**
 * Represents a string value that should not be logged or otherwise exposed, such as an API key.
 * The [toString] implementation redacts the value.
 * Accessing [value] is allowed by opting into [ProtectedString.Access], but please use it with care.
 * Note that this is a simplistic implementation; it does not clean up memory after itself.
 *
 * Note: Serialization will expose the underlying value.
 */
@Suppress("UseDataClass")
@OptIn(ProtectedString.Access::class)
@Serializable(with = ProtectedStringSerializer::class)
public class ProtectedString @Access constructor(
  @Access
  public val value: String,
) {
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

  override fun toString(): String =
    "ProtectedString(value='REDACTED')"
}
