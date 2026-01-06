package kairo.serialization

import com.fasterxml.jackson.databind.module.SimpleModule
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode

internal class KtorModule(
  builder: KairoJson.Builder,
) : SimpleModule() {
  init {
    addSerializer(HttpMethod::class.java, HttpMethodSerializer())
    addDeserializer(HttpMethod::class.java, HttpMethodDeserializer())

    addSerializer(HttpStatusCode::class.java, builder.httpStatusCodeFormat.serializer.value)
    addDeserializer(HttpStatusCode::class.java, builder.httpStatusCodeFormat.deserializer.value)
  }
}
