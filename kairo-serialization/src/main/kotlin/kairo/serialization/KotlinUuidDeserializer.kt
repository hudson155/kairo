package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import kotlin.uuid.Uuid
import kotlin.uuid.toKotlinUuid

internal class KotlinUuidDeserializer : StdDeserializer<Uuid>(
  Uuid::class.java,
) {
  override fun deserialize(
    p: JsonParser,
    ctxt: DeserializationContext,
  ): Uuid {
    val javaUuid = ctxt.readValue(p, java.util.UUID::class.java)
    return javaUuid.toKotlinUuid()
  }
}
