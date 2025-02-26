package kairo.id

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

/**
 * This is a deterministic way of generating Kairo IDs that's useful for tests.
 * They look like "library_book_00000000", and the ID portion itself auto-increments until [reset] is called.
 *
 * Although Kairo IDs are base-62, [DeterministicKairoIdGenerator] only uses numbers.
 */
public class DeterministicKairoIdGenerator(
  prefix: String,
) : KairoIdGenerator(prefix, length) {
  public object Factory : KairoIdGenerator.Factory() {
    private val generators: MutableMap<String, DeterministicKairoIdGenerator> = ConcurrentHashMap()

    override fun withPrefix(prefix: String): DeterministicKairoIdGenerator =
      generators.getOrPut(prefix) { DeterministicKairoIdGenerator(prefix) }

    public fun reset() {
      generators.values.forEach { it.reset() }
    }
  }

  private val seed: AtomicInteger = AtomicInteger(0)

  public fun reset() {
    seed.set(0)
  }

  override fun generate(): KairoId {
    val result = get(seed.getAndIncrement())
    return result
  }

  public operator fun get(id: Int): KairoId =
    generate(prefix, id)

  public companion object {
    private const val length: Int = 8

    public fun generate(prefix: String, id: Int): KairoId =
      KairoId(prefix, id.toString().padStart(length, '0'))
  }
}
