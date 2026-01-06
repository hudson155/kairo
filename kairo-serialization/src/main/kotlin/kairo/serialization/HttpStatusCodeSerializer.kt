package kairo.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import io.ktor.http.HttpStatusCode

public abstract class HttpStatusCodeSerializer : StdSerializer<HttpStatusCode>(
  HttpStatusCode::class.java,
) {
  public class AsNumber : HttpStatusCodeSerializer() {
    override fun serialize(
      value: HttpStatusCode,
      gen: JsonGenerator,
      provider: SerializerProvider,
    ) {
      val int = value.value
      gen.writeNumber(int)
    }
  }

  public class AsObject : HttpStatusCodeSerializer() {
    override fun serialize(
      value: HttpStatusCode,
      gen: JsonGenerator,
      provider: SerializerProvider,
    ) {
      val delegate = HttpStatusCodeObjectDelegate(value.value, value.description)
      provider.defaultSerializeValue(delegate, gen)
    }
  }
}
