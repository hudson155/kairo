package kairo.serialization.module.ktor

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import io.ktor.http.ContentType
import kairo.serialization.util.expectCurrentToken
import kairo.serialization.util.readValue

@Suppress("RedundantNullableReturnType")
public class ContentTypeDeserializer : StdDeserializer<ContentType>(ContentType::class.java) {
  /**
   * Return type is nullable to support subclasses.
   */
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ContentType? {
    expectCurrentToken(p, ctxt, JsonToken.VALUE_STRING)
    val string = p.readValue<String>()
    return ContentType.parse(string)
  }
}
