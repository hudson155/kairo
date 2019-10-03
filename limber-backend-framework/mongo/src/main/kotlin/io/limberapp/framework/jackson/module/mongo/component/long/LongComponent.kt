package io.limberapp.framework.jackson.module.mongo.component.long

import io.limberapp.framework.jackson.module.mongo.Component

internal const val NUMBER_LONG_KEY = "\$numberLong"

internal class LongComponent : Component<Long>(Long::class) {
    override val serializer = Serializer()
    override val deserializer = Deserializer()
}
