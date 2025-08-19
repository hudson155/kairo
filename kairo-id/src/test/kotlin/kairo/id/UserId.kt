package kairo.id

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
internal value class UserId(override val value: String) : Id {
  init {
    require(regex.matches(value)) { "Malformed user ID." }
  }

  internal companion object {
    val regex: Regex = Id.regex(prefix = Regex("user"))
  }
}
