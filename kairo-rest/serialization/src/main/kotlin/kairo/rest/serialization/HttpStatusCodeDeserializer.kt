package kairo.rest.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import io.ktor.http.HttpStatusCode

internal class HttpStatusCodeDeserializer : StdDeserializer<HttpStatusCode>(
  HttpStatusCode::class.java,
) {
  override fun deserialize(
    p: JsonParser,
    ctxt: DeserializationContext,
  ): HttpStatusCode {
    val int = p.intValue
    return HttpStatusCode.fromValue(int)
  }
}
