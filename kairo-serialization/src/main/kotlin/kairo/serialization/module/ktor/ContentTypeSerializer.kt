package kairo.serialization.module.ktor

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import io.ktor.http.ContentType

public class ContentTypeSerializer : StdSerializer<ContentType>(ContentType::class.java) {
  override fun serialize(value: ContentType, gen: JsonGenerator, provider: SerializerProvider) {
    val string = value.toString()
    gen.writeString(string)
  }
}
