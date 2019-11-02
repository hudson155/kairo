package io.limberapp.framework.util

import java.nio.ByteBuffer
import java.util.UUID

class DeterministicUuidGenerator : UuidGenerator {

    private var seed = 0

    fun reset() {
        seed = 0
    }

    override fun generate(): UUID {
        val result = this[seed]
        seed++
        return result
    }

    operator fun get(i: Int): UUID =
        UUID.nameUUIDFromBytes(ByteBuffer.allocate(Int.SIZE_BYTES).apply { putInt(i) }.array())
}
