package kairo.serialization.module.time

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.LocalDate

/**
 * We don't use com.fasterxml.jackson.datatype:jackson-datatype-jsr310,
 * and instead roll our own time module. See [TimeModule].
 *
 * This implementation for [LocalDate] formats strings according to ISO-8601.
 */
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
    @JvmStatic
    private fun convert(value: LocalDate): String =
      value.toString()
  }
}
