package io.limberapp.common.util.uuid

import java.util.UUID

private const val UUID_LENGTH_WITHOUT_DASHES: Int = 32

/**
 * This function is preferred it [UUID.fromString] because it correctly handles UUIDs without
 * dashes, whereas [UUID.fromString] does not.
 */
fun uuidFromString(string: String): UUID {
  val withNoDashes = string.replace("-", "")
  check(withNoDashes.length == UUID_LENGTH_WITHOUT_DASHES)
  @Suppress("ReplaceSubstringWithTake")
  val withDashes = listOf(
      withNoDashes.substring(0, 8),
      withNoDashes.substring(8, 12),
      withNoDashes.substring(12, 16),
      withNoDashes.substring(16, 20),
      withNoDashes.substring(20, 32),
  ).joinToString("-")
  return UUID.fromString(withDashes)
}
