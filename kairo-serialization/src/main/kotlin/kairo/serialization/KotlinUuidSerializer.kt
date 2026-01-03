package kairo.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid

internal class KotlinUuidSerializer : StdSerializer<Uuid>(
  Uuid::class.java,
) {
  override fun serialize(
    value: Uuid,
    gen: JsonGenerator,
    provider: SerializerProvider,
  ) {
    val javaUuid = value.toJavaUuid()
    provider.defaultSerializeValue(javaUuid, gen)
  }
}
