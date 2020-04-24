package com.piperframework.util.darb

/**
 * Converts between boolean lists and the DARB string format. The DARB string format is a format invented by Piper. It
 * stands for Dense(ish) Albeit Readable Binary. It's an encoding that represents binary data using hex digits, prefixed
 * by the length of the data. This prefix is necessary in order to account for data lengths that are not multiples of 4.
 * For examples, see this class's tests.
 */
object DarbEncoder {

    private const val CHUNK_SIZE = 4 // Warning, changing this alone will break the code.

    fun encode(booleanList: List<Boolean>): String {

        // Chunk by 4 because each hex represents 4 bits.
        val chunkedBooleanList = booleanList.chunked(CHUNK_SIZE)

        // Map each chunk of 4 bits to a hex character, then join the characters together.
        val hex = chunkedBooleanList.joinToString("") {

            // intValue is the value of the hex character from 0 to 15 (inclusive).
            var intValue = 0
            if (it.getOrNull(0) == true) intValue += 8
            if (it.getOrNull(1) == true) intValue += 4
            if (it.getOrNull(2) == true) intValue += 2
            if (it.getOrNull(3) == true) intValue += 1

            return@joinToString if (intValue < 10) {
                // 0 through 9 are represented by digits.
                ('0' + intValue)
            } else {
                // 10 through 15 are represented by capital letters A through F.
                ('A' + (intValue - 10))
            }.toString()
        }

        // DARB prefixes the hex characters with the size of the length of the data.
        val size = booleanList.size
        return "$size.$hex"
    }

    fun decode(darb: String): List<Boolean> {

        // DARB always has 2 components separated by a dot, and no dots elsewhere in the syntax.
        val components = darb.split('.')
        require(components.size == 2)

        // The first component is the size (positive).
        val size = components[0].toInt()
        require(size >= 0)

        // The second component is the hex, the length of which must correlate with the size.
        val hex = components[1]
        require(hex.length == (size + CHUNK_SIZE - 1) / CHUNK_SIZE)

        // Map each hex to a list of 4 digits representing the hex in binary.
        val booleanList = hex.flatMap {

            // intValue is the value of the hex character from 0 to 15 (inclusive).
            val intValue = when (it) {
                // 0 through 9 are represented by digits.
                in CharRange('0', '9') -> it.toInt() - '0'.toInt()
                // 10 through 15 are represented by capital letters A through F.
                in CharRange('A', 'F') -> it.toInt() - 'A'.toInt() + 10
                // No other characters are supported.
                else -> throw IllegalArgumentException()
            }

            return@flatMap listOf(
                intValue >= 8, // first bit
                intValue % 8 >= 4, // second bit
                intValue % 4 >= 2, // third bit
                intValue % 2 >= 1 // fourth bit
            )
        }

        // Take the size into account to return a list of the correct length.
        // This will omit between 0 and 3 booleans from the end.
        return booleanList.subList(0, size)
    }
}
