package kairo.darb

/**
 * Encodes a list of booleans into a Dense-ish Albeit Readable Binary (DARB) string.
 * The string contains 2 components, a prefix and a body, separated by a dot.
 *
 * An example DARB string is "5.C8".
 * This string represents the boolean list [true, true, false, false, true].
 * The prefix is "5" and the body is "C8".
 *
 * The prefix indicates the length of the data.
 * Within the body, each character represents 4 booleans
 * (except the last character, which can represent fewer than 4 but not 0 booleans).
 * In the example above, the 1st character "C" maps to 1100 in binary, which represents [true, true, false, false].
 * In the example above, the 2nd character "8" maps to 1000 in binary, which represents [true, false, false, false].
 * However, since there are only 5 booleans in the list (indicated by the prefix), we ignore the trailing booleans.
 *
 * Also decodes; the inverse operation.
 * See the corresponding test for more spec.
 */
public object DarbEncoder {
  private const val CHUNK_SIZE: Int = 4 // Warning, changing this alone will break the code.

  private val regex: Regex = Regex("[A-Fa-f0-9]*")

  public fun encode(booleanList: List<Boolean>): String {
    // Chunk by 4 because each hex represents 4 bits.
    val chunkedBooleanList = booleanList.chunked(CHUNK_SIZE)

    // Map each chunk of 4 bits to a hex character, then join the characters together.
    val body = chunkedBooleanList.joinToString("") { chunk -> encodeChunk(chunk) }

    // DARB prefixes the hex characters with the length of the data.
    val prefix = booleanList.size
    return "$prefix.$body"
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
      else -> IllegalStateException("Internal error in DARB encoder.") // This should never happen.
    }.toString()
  }

  public fun decode(darb: String): List<Boolean> {
    val (size, hex) = getComponents(darb)

    // Map each hex to a list of 4 digits representing the hex in binary.
    val booleanList = hex.flatMap { character -> decodeCharacter(character) }

    // Take the size into account to return a list of the correct length.
    // This will omit between 0 and 3 booleans from the end.
    val result = booleanList.subList(0, size)
    val bitString = BooleanListEncoder.encode(result)
    return result
  }

  private fun getComponents(darb: String): Pair<Int, String> {
    // DARB always has 2 components separated by a dot, and no dots elsewhere in the syntax.
    val components = darb.split('.')
    if (components.size != 2) throw IllegalArgumentException("DARB must have 2 components.")

    // The first component is the size (positive).
    val size = components[0].toInt()
    if (size < 0) throw IllegalArgumentException("DARB size cannot be negative.")

    // The second component is the hex, the length of which must correlate with the size.
    val hex = components[1]
    // This math works due to integer rounding.
    if (hex.length != (size + CHUNK_SIZE - 1) / CHUNK_SIZE) {
      throw IllegalArgumentException("DARB hex length doesn't match size component.")
    }
    if (!this.regex.matches(hex)) {
      throw IllegalArgumentException("Invalid DARB hex.")
    }
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
      else -> throw IllegalStateException("Unsupported character: $character.")
    }

    return listOf(
      intValue >= 8, // First bit.
      intValue % 8 >= 4, // Second bit.
      intValue % 4 >= 2, // Third bit.
      intValue % 2 >= 1, // Fourth bit.
    )
  }
}
