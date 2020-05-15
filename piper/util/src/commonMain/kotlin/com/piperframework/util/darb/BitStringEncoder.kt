package com.piperframework.util.darb

/**
 * Converts between boolean lists and bit string format.
 */
object BitStringEncoder {
  fun encode(booleanList: List<Boolean>): String = booleanList.joinToString("") { if (it) "1" else "0" }

  fun decode(bitString: String): List<Boolean> = bitString.map {
    return@map when (it) {
        '0' -> false
        '1' -> true
        else -> throw IllegalArgumentException()
    }
  }
}
