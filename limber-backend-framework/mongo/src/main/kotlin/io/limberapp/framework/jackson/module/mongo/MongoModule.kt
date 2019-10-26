package io.limberapp.framework.jackson.module.mongo

import com.fasterxml.jackson.databind.module.SimpleModule
import io.limberapp.framework.jackson.module.mongo.component.double.DoubleComponent
import io.limberapp.framework.jackson.module.mongo.component.uuid.UuidComponent
import io.limberapp.framework.jackson.module.mongo.component.localDateTime.LocalDateTimeComponent
import io.limberapp.framework.jackson.module.mongo.component.long.LongComponent

internal class MongoModule : SimpleModule() {

    init {
        install(DoubleComponent())
        install(LocalDateTimeComponent())
        install(LongComponent())
        install(UuidComponent())
    }

    private fun <T : Any> install(component: Component<T>) {
        addSerializer(component.clazz.java, component.serializer)
        addDeserializer(component.clazz.java, component.deserializer)
    }
}
