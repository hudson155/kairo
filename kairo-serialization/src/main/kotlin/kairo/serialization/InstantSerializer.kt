package kairo.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.Instant
import java.time.format.DateTimeFormatter

/**
 * We don't use com.fasterxml.jackson.datatype:jackson-datatype-jsr310,
 * and instead roll our own time module. See [TimeModule].
 *
 * This implementation for [Instant] formats strings using [DateTimeFormatter.ISO_INSTANT].
 */
internal class InstantSerializer : StdSerializer<Instant>(Instant::class.java) {
  override fun serialize(value: Instant, gen: JsonGenerator, provider: SerializerProvider) {
    val formatter = DateTimeFormatter.ISO_INSTANT
    val string = formatter.format(value)
    gen.writeString(string)
  }
}
