package kairo.id

import kotlin.random.Random

/**
 * This is the default/production way of generating IDs.
 */
public class RandomKairoIdGenerator(
  prefix: String,
  private val length: Int,
) : KairoIdGenerator(prefix) {
  public class Factory(
    private val length: Int,
  ) : KairoIdGenerator.Factory() {
    override fun withPrefix(prefix: String): RandomKairoIdGenerator =
      RandomKairoIdGenerator(prefix, length)
  }

  override fun generate(): KairoId =
    KairoId(prefix, randomId())

  private fun randomId(): String =
    buildString {
      repeat(this@RandomKairoIdGenerator.length) {
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
