package kairo.id

import java.util.concurrent.ConcurrentHashMap

/**
 * This is a deterministic way of generating Kairo IDs that's useful for tests.
 * They look like "library_book_00000000", and the ID portion itself auto-increments until [reset] is called.
 *
 * Although Kairo IDs are base-62, [DeterministicKairoIdGenerator] only uses numbers.
 */
public class DeterministicKairoIdGenerator(
  prefix: String,
  length: Int,
) : KairoIdGenerator(prefix, length) {
  public class Factory(
    private val length: Int,
  ) : KairoIdGenerator.Factory() {
    private val generators: MutableMap<String, DeterministicKairoIdGenerator> = ConcurrentHashMap()

    override fun withPrefix(prefix: String): DeterministicKairoIdGenerator =
      generators.getOrPut(prefix) { DeterministicKairoIdGenerator(prefix, length) }

    public fun reset() {
      generators.values.forEach { it.reset() }
    }
  }

  private var seed: Int = 0

  public fun reset() {
    seed = 0
  }

  override fun generate(): KairoId =
    get(seed).also { seed++ }

  public operator fun get(i: Int): KairoId =
    KairoId(prefix, i.toString().padStart(length, '0'))
}
