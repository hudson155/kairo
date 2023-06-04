package limber.util.id

import kotlin.random.Random

private const val ID_LENGTH: Int = 16

/**
 * This is the default/production way of generating IDs.
 */
public class RandomIdGenerator(prefix: String) : IdGenerator(prefix) {
  public class Factory : IdGenerator.Factory {
    override fun invoke(prefix: String): RandomIdGenerator =
      RandomIdGenerator(prefix)
  }

  override fun generate(): String {
    val builder = StringBuilder(ID_LENGTH)
    repeat(ID_LENGTH) {
      val char = when (val seed = Random.nextInt(62)) {
        in 0..9 -> Char(48 + seed) // 0-9
        in 10..35 -> Char(65 - 10 + seed) // A-Z
        in 36..61 -> Char(97 - 36 + seed) // a-z
        else -> error("Implementation error.")
      }
      builder.append(char)
    }
    return "${prefix}_$builder"
  }
}
