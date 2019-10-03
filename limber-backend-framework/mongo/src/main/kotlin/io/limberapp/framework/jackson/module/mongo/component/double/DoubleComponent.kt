package io.limberapp.framework.jackson.module.mongo.component.double

import io.limberapp.framework.jackson.module.mongo.Component

internal const val NUMBER_DECIMAL_KEY = "\$numberDecimal"

internal class DoubleComponent : Component<Double>(Double::class) {
    override val serializer = Serializer()
    override val deserializer = Deserializer()
}
