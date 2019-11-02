package io.limberapp.framework.util

import java.util.UUID

class RandomUuidGenerator : UuidGenerator {

    override fun generate(): UUID = UUID.randomUUID()
}
