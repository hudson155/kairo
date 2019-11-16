package io.limberapp.framework.util.uuidGenerator

import java.util.UUID

/**
 * The UuidGenerator should be used to generate UUIDs, rather than using UUID.randomUUID(). This
 * helps make testing a lot easier.
 */
interface UuidGenerator {

    fun generate(): UUID
}
