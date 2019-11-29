package com.piperframework.mongo

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer
import com.piperframework.util.uuid.asByteArray
import de.undercouch.bson4jackson.BsonGenerator
import java.util.UUID

/**
 * This is copied from de.undercouch.bson4jackson.serializers.BsonUuidSerializer but uses binary subtype 4 instead of
 * binary subtype 3, which also means that it uses big endian byte ordering instead of little endian byte ordering.
 */
class BsonUuidSerializer : JsonSerializer<UUID>() {

    override fun serialize(value: UUID, gen: JsonGenerator, serializers: SerializerProvider) {
        if (gen is BsonGenerator) {
            gen.writeBinary(null, 0x04, value.asByteArray(), 0, 16)
        } else {
            UUIDSerializer().serialize(value, gen, serializers)
        }
    }
}
