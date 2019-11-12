package io.limberapp.framework.util

import java.nio.ByteBuffer
import java.util.UUID

private const val UUID_BYTES = 16

internal fun UUID.asByteArray(): ByteArray = ByteBuffer.wrap(ByteArray(UUID_BYTES)).apply {
    putLong(mostSignificantBits)
    putLong(leastSignificantBits)
}.array()

@Suppress("FunctionName")
internal fun UUID(byteArray: ByteArray) = with(ByteBuffer.wrap(byteArray)) { UUID(long, long) }
