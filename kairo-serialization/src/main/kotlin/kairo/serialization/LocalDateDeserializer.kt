package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * We don't use com.fasterxml.jackson.datatype:jackson-datatype-jsr310,
 * and instead roll our own time module. See [TimeModule].
 *
 * This implementation for [LocalDate] only accepts strings formatted as [DateTimeFormatter.ISO_DATE].
 */
internal class LocalDateDeserializer : StdDeserializer<LocalDate>(LocalDate::class.java) {
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LocalDate {
    expectCurrentToken(p, ctxt, JsonToken.VALUE_STRING)
    val formatter = DateTimeFormatter.ISO_DATE
    val string = p.readValueAs<String>()
    return LocalDate.from(formatter.parse(string))
  }
}
