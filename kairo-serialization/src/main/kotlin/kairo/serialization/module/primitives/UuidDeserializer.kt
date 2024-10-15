package kairo.serialization.module.primitives

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.KeyDeserializer
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import kairo.serialization.util.expectCurrentToken
import kairo.serialization.util.readValue
import kotlin.uuid.Uuid

/**
 * Jackson supports [java.util.UUID] by default, but not [kotlin.uuid.Uuid] which we use.
 */
@Suppress("RedundantNullableReturnType")
public class UuidDeserializer : StdDeserializer<Uuid>(Uuid::class.java) {
  public class Key : KeyDeserializer() {
    override fun deserializeKey(key: String, ctxt: DeserializationContext): Uuid =
      convert(key)
  }

  /**
   * Return type is nullable to support subclasses.
   */
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Uuid? {
    expectCurrentToken(p, ctxt, JsonToken.VALUE_STRING)
    val string = p.readValue<String>()
    return convert(string)
  }

  public companion object {
    private fun convert(string: String): Uuid =
      Uuid.parse(string)
  }
}
