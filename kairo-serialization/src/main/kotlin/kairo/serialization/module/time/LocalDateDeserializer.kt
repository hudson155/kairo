package kairo.serialization.module.time

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kairo.serialization.util.expectCurrentToken
import kairo.serialization.util.readValue

/**
 * We don't use com.fasterxml.jackson.datatype:jackson-datatype-jsr310,
 * and instead roll our own time module. See [TimeModule].
 *
 * This implementation for [LocalDate] only accepts strings formatted as [DateTimeFormatter.ISO_DATE].
 */
@Suppress("RedundantNullableReturnType")
public class LocalDateDeserializer : StdDeserializer<LocalDate>(LocalDate::class.java) {
  /**
   * Return type is nullable to support subclasses.
   */
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LocalDate? {
    expectCurrentToken(p, ctxt, JsonToken.VALUE_STRING)
    val string = p.readValue<String>()
    return convert(string)
  }

  public companion object {
    private fun convert(string: String): LocalDate =
      LocalDate.from(localDateFormatter.parse(string))
  }
}
