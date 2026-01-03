package kairo.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime

internal class KotlinLocalDateTimeSerializer : StdSerializer<LocalDateTime>(
  LocalDateTime::class.java,
) {
  override fun serialize(
    value: LocalDateTime,
    gen: JsonGenerator,
    provider: SerializerProvider,
  ) {
    val javaLocalDateTime = value.toJavaLocalDateTime()
    provider.defaultSerializeValue(javaLocalDateTime, gen)
  }
}
