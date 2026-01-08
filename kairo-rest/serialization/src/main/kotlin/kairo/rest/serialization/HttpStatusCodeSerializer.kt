package kairo.rest.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import io.ktor.http.HttpStatusCode

internal class HttpStatusCodeSerializer : StdSerializer<HttpStatusCode>(
  HttpStatusCode::class.java,
) {
  override fun serialize(
    value: HttpStatusCode,
    gen: JsonGenerator,
    provider: SerializerProvider,
  ) {
    val int = value.value
    gen.writeNumber(int)
  }
}
