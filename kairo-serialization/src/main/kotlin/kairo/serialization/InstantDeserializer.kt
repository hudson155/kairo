package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.time.Instant
import java.time.format.DateTimeFormatter

internal class InstantDeserializer : StdDeserializer<Instant>(Instant::class.java) {
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Instant {
    expectCurrentToken(p, ctxt, JsonToken.VALUE_STRING)
    val formatter = DateTimeFormatter.ISO_INSTANT
    val string = p.readValueAs<String>()
    return Instant.from(formatter.parse(string))
  }
}
