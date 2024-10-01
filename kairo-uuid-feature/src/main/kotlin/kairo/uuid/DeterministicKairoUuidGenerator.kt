package kairo.uuid

import kotlin.uuid.Uuid

/**
 * This is a deterministic way of generating UUID that's useful for tests.
 * They look like "00000000-0000-0000-0000-000000000000", and auto-increment until [reset] is called.
 *
 * Although UUID strings are base-16, [DeterministicKairoUuidGenerator] only uses numbers.
 */
public class DeterministicKairoUuidGenerator : KairoUuidGenerator() {
  private var seed: Int = 0

  public fun reset() {
    seed = 0
  }

  override fun generate(): Uuid {
    val result = get(seed)
    seed++
    return result
  }

  public operator fun get(id: Int): Uuid =
    generate(id)

  public companion object {
    public fun generate(id: Int): Uuid =
      Uuid.parse("00000000-0000-0000-0000-${"$id".padStart(12, '0')}")
  }
}
