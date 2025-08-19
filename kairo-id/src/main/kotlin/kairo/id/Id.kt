package kairo.id

import kotlinx.serialization.Serializable

/**
 * Kairo has a class you can optionally use to represent semantic identifiers for different entities.
 * Think of a Kairo ID as an alternative to a UUID or serial ID, but with a few fun perks.
 *
 * An example Kairo ID is "user_ccU4Rn4DKVjCMqt3d0oAw3".
 * The "semantic" part means that a human can easily understand this is a user ID
 * rather than the ID for a different entity.
 * This is mostly useful for developers, but it can also make URL slugs look nicer for users.
 *
 * Kairo IDs consist of a "prefix" portion and a "value" portion.
 * In the example above, "user_ccU4Rn4DKVjCMqt3d0oAw3" has a prefix of "user"
 * and a value of `ccU4Rn4DKVjCMqt3d0oAw3`.
 */
@Serializable
@JvmInline
public value class Id private constructor(
  /**
   * Use [toString] to access this.
   */
  internal val string: String,
) {
  init {
    require(regex.matches(string)) { "Invalid ID (string=$string)." }
  }

  override fun toString(): String =
    string

  public companion object {
    internal val regex: Regex = regex()

    public fun regex(prefix: Regex = Regex("[a-z][a-z0-9]*(_[a-z][a-z0-9]*)*")): Regex {
      val value = Regex("[A-Za-z0-9]+")
      return Regex("(?<prefix>$prefix)_(?<value>$value)")
    }

    public fun of(prefix: String, value: String): Id =
      Id("${prefix}_$value")

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
