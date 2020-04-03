package io.limberapp.backend.module.healthCheck.rep.healthCheck

import com.piperframework.rep.CompleteRep
import com.piperframework.serialization.LocalDateTimeSerializer
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import java.time.LocalDateTime

internal object HealthCheckRep {

    @Serializable
    enum class State {
        HEALTHY,
        UNHEALTHY;

        @Serializer(forClass = State::class)
        companion object : KSerializer<State> {
            override val descriptor = PrimitiveDescriptor(State::class.simpleName!!, PrimitiveKind.STRING)
            override fun serialize(encoder: Encoder, value: State) = encoder.encodeString(value.name)
            override fun deserialize(decoder: Decoder) = valueOf(decoder.decodeString())
        }
    }

    @Serializable
    data class Complete(
        @Serializable(with = LocalDateTimeSerializer::class)
        override val created: LocalDateTime,
        val state: State
    ) : CompleteRep
}
