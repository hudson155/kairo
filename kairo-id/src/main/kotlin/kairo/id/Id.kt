package kairo.id

/**
 * Kairo IDs are an optional way of managing unique identifiers.
 * Think of a Kairo ID as an alternative to a UUID or serial ID.
 *
 * An example Kairo ID is "user_ccU4Rn4DKVjCMqt3d0oAw3".
 * A human can easily understand that this is a user ID rather than the ID for a different entity.
 * This is mostly useful for developers, but it can also make URL slugs look nicer and stuff.
 *
 * Kairo IDs consist of a "prefix" portion and a "payload" portion.
 * In the example above, "user_ccU4Rn4DKVjCMqt3d0oAw3" has a prefix of "user"
 * and a payload of `ccU4Rn4DKVjCMqt3d0oAw3`.
 */
public interface Id {
  public val value: String

  public companion object {
    public fun regex(prefix: Regex): Regex =
      Regex("(?<prefix>(?=[a-z][a-z0-9]*(_[a-z][a-z0-9]*)*)$prefix)_(?<payload>[A-Za-z0-9]+)")
  }
}
