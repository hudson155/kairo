package kairo.serialization.module.ktor

import com.fasterxml.jackson.databind.module.SimpleModule
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import org.javamoney.moneta.Money

/**
 * Configures the Kairo-recognized [Money] type.
 */
internal class KtorModule private constructor() : SimpleModule() {
  init {
    configureContentType()
    configureHttpMethod()
  }

  private fun configureContentType() {
    addSerializer(ContentType::class.javaObjectType, ContentTypeSerializer())
    addDeserializer(ContentType::class.javaObjectType, ContentTypeDeserializer())
  }

  private fun configureHttpMethod() {
    addSerializer(HttpMethod::class.javaObjectType, HttpMethodSerializer())
    addDeserializer(HttpMethod::class.javaObjectType, HttpMethodDeserializer())
  }

  internal companion object {
    fun create(): KtorModule =
      KtorModule()
  }
}
