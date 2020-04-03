package io.limberapp.backend.authorization.principal

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
enum class JwtRole {

    IDENTITY_PROVIDER,
    SUPERUSER;

    @Serializer(forClass = JwtRole::class)
    companion object : KSerializer<JwtRole> {

        override val descriptor = PrimitiveDescriptor(JwtRole::class.simpleName!!, PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: JwtRole) {
            encoder.encodeString(value.name)
        }

        override fun deserialize(decoder: Decoder): JwtRole {
            return valueOf(decoder.decodeString())
        }
    }
}
