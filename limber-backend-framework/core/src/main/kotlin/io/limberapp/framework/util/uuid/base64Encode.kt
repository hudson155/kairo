package io.limberapp.framework.util.uuid

import java.nio.ByteBuffer
import java.util.Base64
import java.util.UUID

private const val UUID_BYTES = 16

fun UUID.base64Encode(): String {
    val encoder = Base64.getEncoder()
    val byteBuffer = ByteBuffer.wrap(ByteArray(UUID_BYTES))
    byteBuffer.apply {
        putLong(mostSignificantBits)
        putLong(leastSignificantBits)
    }
    val byteArray = byteBuffer.array()
    return encoder.encodeToString(byteArray)
}
