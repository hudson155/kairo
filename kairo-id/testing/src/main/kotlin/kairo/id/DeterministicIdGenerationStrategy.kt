package kairo.id

import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.fetchAndIncrement

/**
 * Deterministic ID generation is useful for tests.
 */
public class DeterministicIdGenerationStrategy(
  private val length: Int?,
) : IdGenerationStrategy() {
  private val index: AtomicInt = AtomicInt(0)

  override fun generate(length: Int?): String {
    val computedLength = requireNotNull(length ?: this@DeterministicIdGenerationStrategy.length) {
      "Must specify length."
    }
    return index.fetchAndIncrement().toString().padStart(computedLength, '0')
  }
}
