package io.limberapp.framework.jackson.module.mongo.component.localDateTime

import io.limberapp.framework.jackson.module.mongo.Component
import java.time.LocalDateTime

internal const val MILLIS_PER_SECOND = 1_000
internal const val NANOS_PER_MILLI = 1_000

internal const val DATE_KEY = "\$date"

internal class LocalDateTimeComponent : Component<LocalDateTime>(LocalDateTime::class) {
    override val serializer = Serializer()
    override val deserializer = Deserializer()
}
