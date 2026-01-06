package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import io.ktor.http.HttpMethod

public class HttpMethodDeserializer : StdDeserializer<HttpMethod>(
  HttpMethod::class.java,
) {
  override fun deserialize(
    p: JsonParser,
    ctxt: DeserializationContext,
  ): HttpMethod {
    val string = p.text
    return HttpMethod.parse(string)
  }
}
