package kairo.doNotLogString

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import kairo.hashing.md5

/**
 * Represents a string value that should not be logged in its entirety because it's very long.
 * The [toString] implementation truncates the value.
 */
@Suppress("UseDataClass")
public class DoNotLogString
@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
constructor(
  @JsonValue public val value: String,
) {
  override fun equals(other: Any?): Boolean {
    if (other !is DoNotLogString) return false
    if (value != other.value) return false
    return true
  }

  override fun hashCode(): Int =
    value.hashCode()

  override fun toString(): String {
    val hash = value.md5()
    val length = value.length
    val truncated = buildString {
      append(value.take(40))
      if (length > 40) append("...")
    }
    return "DoNotLogString(hash='$hash', length=$length, truncated='$truncated')"
  }
}
