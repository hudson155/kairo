package kairo.id

import kotlin.random.Random

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

  public abstract class Companion<T : Id>(
    private val length: Int = 15,
  ) {
    init {
      require(length in 8..32) { "Invalid ID length (length=$length). Must be between 8 and 32 (inclusive)." }
    }

    public fun random(): T {
      val payload = buildString {
        repeat(this@Companion.length) {
          val char = when (val seed = Random.nextInt(62)) {
            in 0..9 -> Char(48 + seed) // 0-9
            in 10..35 -> Char(65 - 10 + seed) // A-Z
            in 36..61 -> Char(97 - 36 + seed) // a-z
            else -> error("Implementation error.")
          }
          append(char)
        }
      }
      return create(payload)
    }

    protected abstract fun create(payload: String): T

    protected fun regex(prefix: Regex): Regex =
      Regex("(?<prefix>(?=[a-z][a-z0-9]*(_[a-z][a-z0-9]*)*)$prefix)_(?<payload>[A-Za-z0-9]+)")
  }
}
