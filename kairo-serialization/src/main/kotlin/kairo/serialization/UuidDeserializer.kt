package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import kotlin.uuid.Uuid

/**
 * Jackson supports [java.util.UUID] by default, but not [kotlin.uuid.Uuid] which we use.
 */
internal class UuidDeserializer : StdDeserializer<Uuid>(Uuid::class.java) {
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Uuid {
    expectCurrentToken(p, ctxt, JsonToken.VALUE_STRING)
    val string = p.readValueAs<String>()
    return Uuid.parse(string)
  }
}
