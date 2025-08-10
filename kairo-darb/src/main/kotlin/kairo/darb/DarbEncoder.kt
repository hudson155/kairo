package kairo.darb

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger: KLogger = KotlinLogging.logger {}

/**
 * Encodes a boolean list into a Dense-ish Albeit Readable Binary (DARB) string.
 * It's a medium-density way to encode boolean lists while maintaining some level of readability.
 *
 * A DARB string contains 2 components, a prefix and a body, separated by a dot.
 * An example DARB string is "23.2CB08E".
 * This string represents the boolean list "0010 1100 1011 0000 1000 111".
 * The prefix is "23" and the body is "2CB08E".
 *
 * The prefix indicates the length of the boolean list. In this case, there are 23 booleans.
 * Within the body, each character represents up to 4 booleans.
 * The first character ("2") maps to "0010".
 * The second character ("C") maps to "1100".
 * The third character ("B") maps to "1011".
 * The fourth character ("0") maps to "0000".
 * The fifth character ("8") maps to "1000".
 * But because the length of this string is only `23`, we ignore the last boolean.
 *
 * Also decodes; the inverse operation.
 * See the corresponding test for more spec.
 */
public object DarbEncoder {
  private const val chunkSize: Int = 4 // Warning, changing this alone will break the code.

  private val regex: Regex = Regex("[A-Fa-f0-9]*")

  public fun encode(booleanList: List<Boolean>): String {
    // Chunk by 4 because each hex represents 4 bits.
    val chunkedBooleanList = booleanList.chunked(chunkSize)

    // Map each chunk of 4 bits to a hex character, then join the characters together.
    val body = chunkedBooleanList.joinToString("") { encodeChunk(it) }

    // DARB prefixes the hex characters with the length of the data.
    val prefix = booleanList.size
    val result = "$prefix.$body"
    logger.debug { "Encoded boolean list $booleanList to DARB $result." }
    return result
  }

  private fun encodeChunk(chunk: List<Boolean>): String {
    // intValue is the value of the hex character from 0 to 15 (inclusive).
    var intValue = 0
    if (chunk.getOrNull(0) == true) intValue += 8
    if (chunk.getOrNull(1) == true) intValue += 4
    if (chunk.getOrNull(2) == true) intValue += 2
    if (chunk.getOrNull(3) == true) intValue += 1

    return when (intValue) {
      in 0..9 -> '0' + intValue
      in 10..15 -> 'A' + (intValue - 10)
      else -> throw IllegalStateException("Internal error in DARB encoder.") // This should never happen.
    }.toString()
  }

  public fun decode(darb: String): List<Boolean> {
    val (size, hex) = getComponents(darb)

    // Map each hex to a list of 4 digits representing the hex in binary.
    val booleanList = hex.flatMap { decodeCharacter(it) }

    // Take the size into account to return a list of the correct length.
    // This will omit between 0 and 3 booleans from the end.
    val result = booleanList.subList(0, size)
    logger.debug { "Decoded DARB $darb to boolean list $result." }
    return result
  }

  private fun getComponents(darb: String): Pair<Int, String> {
    // DARB always has 2 components separated by a dot, and no dots elsewhere in the syntax.
    val components = darb.split('.')
    require(components.size == 2) { "DARB must have 2 components." }

    // The first component is the size (positive).
    val size = components[0].toInt()
    require(size >= 0) { "DARB size cannot be negative." }

    // The second component is the hex, the length of which must correlate with the size.
    val hex = components[1]
    // This math works due to integer rounding.
    require(hex.length == (size + chunkSize - 1) / chunkSize) { "DARB hex length doesn't match size component." }
    require(regex.matches(hex)) { "Invalid DARB hex." }
    return Pair(size, hex)
  }

  private fun decodeCharacter(character: Char): List<Boolean> {
    // intValue is the value of the hex character from 0 to 15 (inclusive).
    val intValue = when (character) {
      // 0 through 9 are represented by digits.
      in CharRange('0', '9') -> character.code - '0'.code
      // 10 through 15 are represented by capital letters A through F.
      in CharRange('A', 'F') -> character.code - 'A'.code + 10
      in CharRange('a', 'f') -> character.code - 'a'.code + 10
      // No other characters are supported.
      // This should never happen because [regex] validates the input.
      else -> error("Unsupported DARB character: $character.")
    }

    return listOf(
      intValue >= 8, // First bit.
      intValue % 8 >= 4, // Second bit.
      intValue % 4 >= 2, // Third bit.
      intValue % 2 >= 1, // Fourth bit.
    )
  }
}
