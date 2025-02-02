package kairo.serialization.module.time

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.KeyDeserializer
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.time.LocalDate
import kairo.serialization.util.expectCurrentToken
import kairo.serialization.util.readValue

/**
 * We don't use com.fasterxml.jackson.datatype:jackson-datatype-jsr310,
 * and instead roll our own time module. See [TimeModule].
 *
 * This implementation for [LocalDate] only accepts strings formatted according to ISO-8601.
 */
@Suppress("RedundantNullableReturnType")
public class LocalDateDeserializer : StdDeserializer<LocalDate>(LocalDate::class.java) {
  public class Key : KeyDeserializer() {
    override fun deserializeKey(key: String, ctxt: DeserializationContext): LocalDate =
      convert(key)
  }

  /**
   * Return type is nullable to support subclasses.
   */
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LocalDate? {
    expectCurrentToken(p, ctxt, JsonToken.VALUE_STRING)
    val string = p.readValue<String>()
    return convert(string)
  }

  public companion object {
    @JvmStatic
    private fun convert(string: String): LocalDate =
      LocalDate.parse(string)
  }
}
