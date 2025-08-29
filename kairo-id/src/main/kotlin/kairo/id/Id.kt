package kairo.id

/**
 * Kairo IDs are an alternative to raw UUIDs or serial IDs.
 * Semantic prefixes (user, business) tell you what they represent.
 * Strong entropy (as much or more randomness than UUIDs, tunable by payload length).
 * Compile-time safety without runtime overhead.
 *
 * An example Kairo ID is "user_ccU4Rn4DKVjCMqt3d0oAw3".
 * Prefix: "user". Payload: "ccU4Rn4DKVjCMqt3d0oAw3".
 */
public interface Id {
  public val value: String

  public companion object {
    public fun regex(prefix: Regex): Regex =
      Regex("(?<prefix>(?=[a-z][a-z0-9]*(_[a-z][a-z0-9]*)*)$prefix)_(?<payload>[A-Za-z0-9]+)")
  }
}
