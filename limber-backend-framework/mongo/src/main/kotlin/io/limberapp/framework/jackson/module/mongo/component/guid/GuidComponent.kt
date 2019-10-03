package io.limberapp.framework.jackson.module.mongo.component.guid

import io.limberapp.framework.jackson.module.mongo.Component
import java.util.UUID

internal const val BINARY_KEY = "\$binary"
internal const val TYPE_KEY = "\$type"
internal const val TYPE_VALUE = "03"

internal class GuidComponent : Component<UUID>(UUID::class) {
    override val serializer = Serializer()
    override val deserializer = Deserializer()
}
