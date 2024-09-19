package kairo.serialization.module.time

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.time.Instant
import java.time.format.DateTimeFormatter
import kairo.serialization.util.expectCurrentToken
import kairo.serialization.util.readValue

/**
 * We don't use com.fasterxml.jackson.datatype:jackson-datatype-jsr310,
 * and instead roll our own time module. See [TimeModule].
 *
 * This implementation for [Instant] only accepts strings formatted as [DateTimeFormatter.ISO_INSTANT].
 */
public class InstantDeserializer : StdDeserializer<Instant>(Instant::class.java) {
  /**
   * Return type is nullable to support subclasses.
   */
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Instant? {
    expectCurrentToken(p, ctxt, JsonToken.VALUE_STRING)
    val formatter = DateTimeFormatter.ISO_INSTANT
    val string = p.readValue<String>()
    return Instant.from(formatter.parse(string))
  }
}
