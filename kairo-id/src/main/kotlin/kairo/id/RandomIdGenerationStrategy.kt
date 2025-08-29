package kairo.id

import kotlin.random.Random

/**
 * Random ID generation is the strategy used for production.
 */
public class RandomIdGenerationStrategy(
  private val length: Int?,
) : IdGenerationStrategy() {
  override fun generate(length: Int?): String =
    buildString {
      val computedLength = requireNotNull(length ?: this@RandomIdGenerationStrategy.length) {
        "Must specify length."
      }
      repeat(computedLength) {
        val char = when (val seed = Random.nextInt(62)) {
          in 0..9 -> Char(48 + seed) // 0-9
          in 10..35 -> Char(65 - 10 + seed) // A-Z
          in 36..61 -> Char(97 - 36 + seed) // a-z
          else -> error("Implementation error.")
        }
        append(char)
      }
    }
}
