package io.limberapp.common.util.uuid

import java.util.*

/**
 * The UuidGenerator should be used to generate UUIDs, rather than using UUID.randomUUID(). This helps make testing a
 * lot easier.
 */
interface UuidGenerator {
  fun generate(): UUID
}
