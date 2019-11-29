package com.piperframework.util.uuid

import java.nio.ByteBuffer
import java.util.UUID

const val UUID_BYTES = 16

fun UUID.asByteArray(): ByteArray = ByteBuffer.wrap(ByteArray(UUID_BYTES)).apply {
    putLong(mostSignificantBits)
    putLong(leastSignificantBits)
}.array()

@Suppress("FunctionName")
fun UUID(byteArray: ByteArray) = with(ByteBuffer.wrap(byteArray)) { UUID(long, long) }
