package kairo.uuid

import java.util.concurrent.atomic.AtomicInteger
import kotlin.uuid.Uuid

/**
 * This is a deterministic way of generating UUID that's useful for tests.
 * They look like "00000000-0000-0000-0000-000000000000", and auto-increment until [reset] is called.
 *
 * Although UUID strings are base-16, [DeterministicKairoUuidGenerator] only uses numbers.
 */
public class DeterministicKairoUuidGenerator : KairoUuidGenerator() {
  private val seed: AtomicInteger = AtomicInteger(0)

  public fun reset() {
    seed.set(0)
  }

  override fun generate(): Uuid =
    get(seed.getAndIncrement())

  public operator fun get(id: Int): Uuid =
    generate(id)

  public companion object {
    public fun generate(id: Int): Uuid =
      Uuid.parse("00000000-0000-0000-0000-${id.toString().padStart(12, '0')}")
  }
}
