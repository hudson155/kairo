package kairo.serialization.module.ktor

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import io.ktor.http.HttpMethod
import kairo.serialization.util.expectCurrentToken
import kairo.serialization.util.readValue

@Suppress("RedundantNullableReturnType")
public class HttpMethodDeserializer : StdDeserializer<HttpMethod>(HttpMethod::class.java) {
  /**
   * Return type is nullable to support subclasses.
   */
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): HttpMethod? {
    expectCurrentToken(p, ctxt, JsonToken.VALUE_STRING)
    val string = p.readValue<String>()
    val result = HttpMethod.parse(string)
    require(result in HttpMethod.DefaultMethods) { "Unrecognized HTTP method: $result." }
    return result
  }
}
