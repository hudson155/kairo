package kairo.id

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

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
) : Comparable<KairoId> {
  init {
    require(prefixRegex.matches(prefix)) { "Invalid prefix component: $prefix." }
    require(idRegex.matches(id)) { "Invalid ID component: $id." }
  }

  override fun compareTo(other: KairoId): Int =
    toString().compareTo(other.toString())

  override fun equals(other: Any?): Boolean {
    if (other !is KairoId) return false
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
    public val length: IntRange = 8..32

    private val prefixRegex: Regex = Regex("[a-z]+(?:_[a-z]+)*")
    private val idRegex: Regex = Regex("[A-Za-z0-9]{${length.first},${length.last}}")
    public val regex: Regex = Regex("($prefixRegex)_($idRegex)")

    public fun isValid(string: String): Boolean = regex.matches(string)

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    @JvmStatic
    public fun fromString(string: String): KairoId {
      val match = requireNotNull(regex.matchEntire(string)) { "Invalid Kairo ID: $string." }
      return KairoId(match.groupValues[1], match.groupValues[2])
    }
  }
}
