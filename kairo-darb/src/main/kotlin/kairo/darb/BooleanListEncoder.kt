package kairo.darb

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger: KLogger = KotlinLogging.logger {}

/**
 * Encodes a list of booleans into a string where each boolean from the list is represented by a 0 or a 1.
 *
 * Also decodes; the inverse operation.
 * See the corresponding test for more spec.
 */
internal object BooleanListEncoder {
  fun encode(booleanList: List<Boolean>): String {
    val result = booleanList.joinToString("") { if (it) "1" else "0" }
    logger.debug { "Encoded boolean list to string (booleanList=$booleanList, result=$result)." }
    return result
  }

  fun decode(string: String): List<Boolean> {
    val result = string.map { character ->
      when (character) {
        '0' -> false
        '1' -> true
        else -> throw IllegalArgumentException("Boolean list strings must only consist of 0s and 1s.")
      }
    }
    logger.debug { "Decoded string to boolean list (string=$string, result=$result)." }
    return result
  }
}
