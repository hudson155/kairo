package kairo.serialization.module.time

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
@Suppress("RedundantNullableReturnType")
public class InstantSerializer : StdSerializer<Instant>(Instant::class.java) {
  public class Key : StdSerializer<Instant>(Instant::class.java) {
    override fun serialize(value: Instant, gen: JsonGenerator, provider: SerializerProvider) {
      val string = convert(value)
      gen.writeFieldName(string)
    }
  }

  override fun serialize(value: Instant, gen: JsonGenerator, provider: SerializerProvider) {
    val string = convert(value)
    gen.writeString(string)
  }

  public companion object {
    private fun convert(value: Instant): String =
      instantFormatter.format(value)
  }
}
