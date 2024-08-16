package kairo.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * We don't use com.fasterxml.jackson.datatype:jackson-datatype-jsr310,
 * and instead roll our own time module. See [TimeModule].
 *
 * This implementation for [LocalDate] formats strings using [DateTimeFormatter.ISO_DATE].
 */
internal class LocalDateSerializer : StdSerializer<LocalDate>(LocalDate::class.java) {
  override fun serialize(value: LocalDate, gen: JsonGenerator, provider: SerializerProvider) {
    val formatter = DateTimeFormatter.ISO_DATE
    val string = formatter.format(value)
    gen.writeString(string)
  }
}
