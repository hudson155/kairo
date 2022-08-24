package limber.type

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

@Suppress("UseDataClass")
public class ProtectedString @JsonCreator(mode = JsonCreator.Mode.DELEGATING) constructor(
  @JsonValue public val value: String,
) {
  override fun equals(other: Any?): Boolean {
    if (other !is ProtectedString) return false
    return value == other.value
  }

  override fun hashCode(): Int =
    value.hashCode()

  override fun toString(): String =
    "REDACTED"
}
