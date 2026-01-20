package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.KeyDeserializer
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import kotlin.uuid.Uuid

internal class KotlinUuidDeserializer : StdDeserializer<Uuid>(
  Uuid::class.java,
) {
  internal class Key : KeyDeserializer() {
    override fun deserializeKey(key: String, ctxt: DeserializationContext): Uuid =
      Uuid.parse(key)
  }

  override fun deserialize(
    p: JsonParser,
    ctxt: DeserializationContext,
  ): Uuid {
    val string = p.text
    return Uuid.parse(string)
  }
}
