package limber.util.id

import java.util.UUID

/**
 * This is a deterministic way of generating GUIDs that's useful for tests.
 */
public class DeterministicGuidGenerator : GuidGenerator {
  private var seed: Int = 0

  public fun reset() {
    seed = 0
  }

  override fun generate(): UUID {
    val result = this[seed]
    seed++
    return result
  }

  public operator fun get(i: Int): UUID =
    UUID.fromString("00000000-0000-0000-abcd-${i.toString().padStart(12, '0')}")
}
