package kairo.serialization

import com.fasterxml.jackson.databind.module.SimpleModule
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.uuid.Uuid

internal class KairoKotlinModule(
  builder: KairoJson.Builder,
) : SimpleModule() {
  init {
    addSerializer(BigDecimal::class.java, builder.bigDecimalFormat.serializer.value)
    addDeserializer(BigDecimal::class.java, builder.bigDecimalFormat.deserializer.value)

    addSerializer(BigInteger::class.java, builder.bigIntegerFormat.serializer.value)
    addDeserializer(BigInteger::class.java, builder.bigIntegerFormat.deserializer.value)

    addSerializer(Uuid::class.java, KotlinUuidSerializer())
    addDeserializer(Uuid::class.java, KotlinUuidDeserializer())
  }
}
