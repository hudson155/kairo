package kairo.id

import kotlinx.serialization.Serializable

/**
 * Kairo IDs are an optional way to uniquely identify entities.
 * Think of them as an alternative to UUIDs or serial IDs.
 * Kairo IDs consist of a "prefix" portion and a "value" portion.
 * The "prefix" portion is context-specific,
 * and the "value" portion is a base-62 string.
 *
 * An example Kairo ID is "user_2eDS1sMt".
 * Notice that a human can immediately understand that this is a
 */
@Serializable
@JvmInline
public value class Id private constructor(
  internal val string: String,
) {
  init {
    require(regex.matches(string)) { "Invalid ID (string=$string)." }
  }

  override fun toString(): String =
    string

  public companion object {
    public val regex: Regex = Regex("(?<prefix>[a-z][a-z0-9]*(_[a-z][a-z0-9]*)*)_(?<value>[A-Za-z0-9]+)")

    public fun parse(string: String): Id =
      Id(string)
  }
}

public fun Id.getPrefix(): String {
  val match = checkNotNull(Id.regex.matchEntire(string))
  return checkNotNull(match.groups["prefix"]).value
}

public fun Id.getValue(): String {
  val match = checkNotNull(Id.regex.matchEntire(string))
  return checkNotNull(match.groups["value"]).value
}
