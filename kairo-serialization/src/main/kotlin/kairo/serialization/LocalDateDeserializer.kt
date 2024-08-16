package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal class LocalDateDeserializer : StdDeserializer<LocalDate>(LocalDate::class.java) {
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LocalDate {
    expectCurrentToken(p, ctxt, JsonToken.VALUE_STRING)
    val formatter = DateTimeFormatter.ISO_DATE
    val string = p.readValueAs<String>()
    return LocalDate.from(formatter.parse(string))
  }
}