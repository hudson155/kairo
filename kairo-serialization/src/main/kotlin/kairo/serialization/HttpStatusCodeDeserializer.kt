package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import io.ktor.http.HttpStatusCode

public abstract class HttpStatusCodeDeserializer : StdDeserializer<HttpStatusCode>(
  HttpStatusCode::class.java,
) {
  public class AsNumber : HttpStatusCodeDeserializer() {
    override fun deserialize(
      p: JsonParser,
      ctxt: DeserializationContext,
    ): HttpStatusCode {
      val int = p.intValue
      return HttpStatusCode.fromValue(int)
    }
  }

  public class AsObject : HttpStatusCodeDeserializer() {
    override fun deserialize(
      p: JsonParser,
      ctxt: DeserializationContext,
    ): HttpStatusCode {
      val delegate = ctxt.readValue(p, HttpStatusCodeObjectDelegate::class.java)
      return HttpStatusCode.fromValue(delegate.value)
    }
  }
}
