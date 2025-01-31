package kairo.hugeString

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import kairo.hashing.md5

/**
 * Represents a string value that should not be logged or otherwise exposed, such as an API key.
 * The [toString] implementation redacts the value.
 */
@Suppress("UseDataClass")
public class HugeString
@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
constructor(
  @JsonValue public val value: String,
) {
  override fun equals(other: Any?): Boolean {
    if (other !is HugeString) return false
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
    return "HugeString(hash='$hash', length=$length, truncated='$truncated')"
  }
}
