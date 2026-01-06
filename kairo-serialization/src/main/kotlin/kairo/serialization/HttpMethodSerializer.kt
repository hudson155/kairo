package kairo.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import io.ktor.http.HttpMethod

public class HttpMethodSerializer : StdSerializer<HttpMethod>(
  HttpMethod::class.java,
) {
  override fun serialize(
    value: HttpMethod,
    gen: JsonGenerator,
    provider: SerializerProvider,
  ) {
    val string = value.value
    gen.writeString(string)
  }
}
