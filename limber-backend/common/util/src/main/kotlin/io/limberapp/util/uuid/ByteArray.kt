package io.limberapp.util.uuid

import java.nio.ByteBuffer
import java.util.UUID

private const val UUID_BYTES: Int = 16

/**
 * https://stackoverflow.com/questions/17893609/convert-uuid-to-byte-that-works-when-using-uuid-nameuuidfrombytesb.
 */
internal fun UUID.asByteArray(): ByteArray {
  val byteArray = ByteArray(UUID_BYTES)
  val byteBuffer = ByteBuffer.wrap(byteArray)
  byteBuffer.putLong(mostSignificantBits)
  byteBuffer.putLong(leastSignificantBits)
  return byteBuffer.array()
}

internal fun uuidFromByteArray(byteArray: ByteArray): UUID {
  val byteBuffer = ByteBuffer.wrap(byteArray)
  return UUID(byteBuffer.long, byteBuffer.long)
}
