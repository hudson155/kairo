package kairo.rest.serialization

import com.fasterxml.jackson.databind.module.SimpleModule
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kairo.serialization.HttpMethodDeserializer
import kairo.serialization.HttpMethodSerializer
import kairo.serialization.HttpStatusCodeDeserializer
import kairo.serialization.HttpStatusCodeSerializer

internal class RestModule : SimpleModule() {
  init {
    addSerializer(HttpMethod::class.java, HttpMethodSerializer())
    addDeserializer(HttpMethod::class.java, HttpMethodDeserializer())

    addSerializer(HttpStatusCode::class.java, HttpStatusCodeSerializer())
    addDeserializer(HttpStatusCode::class.java, HttpStatusCodeDeserializer())
  }
}
