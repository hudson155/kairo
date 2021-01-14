package io.limberapp.common.util.uuid

import java.util.UUID

private const val UUID_LENGTH_WITH_DASHES = 36
private const val UUID_LENGTH_WITHOUT_DASHES = 32

/**
 * This function is preferred it [UUID.fromString] because it correctly handles UUIDs without
 * dashes, whereas [UUID.fromString] does not.
 */
fun uuidFromString(string: String): UUID {
  if (string.length == UUID_LENGTH_WITH_DASHES) return UUID.fromString(string)
  val withNoDashes = string.replace("-", "")
  check(withNoDashes.length == UUID_LENGTH_WITHOUT_DASHES)
  val withDashes = listOf(
      withNoDashes.substring(0, 8),
      withNoDashes.substring(8, 12),
      withNoDashes.substring(12, 16),
      withNoDashes.substring(16, 20),
      withNoDashes.substring(20, 32),
  ).joinToString("-")
  return UUID.fromString(withDashes)
}
