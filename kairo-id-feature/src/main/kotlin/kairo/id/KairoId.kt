package kairo.id

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

internal val kairoIdLength: IntRange = 8..32

private val prefixRegex: Regex = Regex("[a-z]+(?:_[a-z]+)*")

private val idRegex: Regex = Regex("[A-Za-z0-9]{${kairoIdLength.first},${kairoIdLength.last}}")

private val fullRegex: Regex = Regex("($prefixRegex)_($idRegex)")

/**
 * Kairo IDs are an optional way to uniquely identify entities.
 * Think of them as an alternative to `UUID`s or serial IDs.
 * Kairo IDs consist of a [prefix] portion and an [id] portion.
 * The [prefix] portion is context-specific,
 * and the [id] portion is a base-62 string.
 */
@Suppress("UseDataClass")
public class KairoId(
  public val prefix: String,
  public val id: String,
) {
  init {
    require(prefixRegex.matches(prefix)) { "Invalid prefix component: $prefix." }
    require(idRegex.matches(id)) { "Invalid ID component: $id." }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as KairoId

    if (prefix != other.prefix) return false
    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int {
    var result = prefix.hashCode()
    result = 31 * result + id.hashCode()
    return result
  }

  @JsonValue
  override fun toString(): String =
    listOf(prefix, id).joinToString("_")

  public companion object {
    @JsonCreator
    @JvmStatic
    public fun fromString(string: String): KairoId {
      val match = requireNotNull(fullRegex.matchEntire(string)) { "Invalid Kairo ID: $string." }
      return KairoId(match.groupValues[1], match.groupValues[2])
    }
  }
}
