package kairo.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal class InstantSerializer : StdSerializer<Instant>(Instant::class.java) {
  override fun serialize(value: Instant, gen: JsonGenerator, provider: SerializerProvider) {
    val formatter = DateTimeFormatter.ISO_INSTANT
    val string = formatter.format(value)
    gen.writeString(string)
  }
}
