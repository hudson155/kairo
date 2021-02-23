package io.limberapp.util.uuid

import java.util.UUID

/**
 * This is the default/production way of generating UUIDs.
 */
class RandomUuidGenerator : UuidGenerator {
  override fun generate(): UUID = UUID.randomUUID()
}
