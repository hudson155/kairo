package com.piperframework.mongo

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.piperframework.util.uuid.UUID_BYTES
import com.piperframework.util.uuid.asByteArray
import de.undercouch.bson4jackson.BsonGenerator
import org.bson.BsonBinarySubType
import java.util.UUID

class BsonUuidSerializer : JsonSerializer<UUID>() {

    override fun serialize(value: UUID, gen: JsonGenerator, serializers: SerializerProvider) {
        check(gen is BsonGenerator)
        gen.writeBinary(null, BsonBinarySubType.UUID_STANDARD.value, value.asByteArray(), 0, UUID_BYTES)
    }
}
