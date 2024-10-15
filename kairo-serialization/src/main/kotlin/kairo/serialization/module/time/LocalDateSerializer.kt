package kairo.serialization.module.time

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
@Suppress("RedundantNullableReturnType")
public class LocalDateSerializer : StdSerializer<LocalDate>(LocalDate::class.java) {
  public class Key : StdSerializer<LocalDate>(LocalDate::class.java) {
    override fun serialize(value: LocalDate, gen: JsonGenerator, provider: SerializerProvider) {
      val string = convert(value)
      gen.writeFieldName(string)
    }
  }

  override fun serialize(value: LocalDate, gen: JsonGenerator, provider: SerializerProvider) {
    val string = convert(value)
    gen.writeString(string)
  }

  public companion object {
    private fun convert(value: LocalDate): String =
      localDateFormatter.format(value)
  }
}
