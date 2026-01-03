package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime

internal class KotlinLocalDateTimeDeserializer : StdDeserializer<LocalDateTime>(
  LocalDateTime::class.java
) {
  override fun deserialize(
    p: JsonParser,
    ctxt: DeserializationContext,
  ): LocalDateTime {
    val javaLocalDateTime = ctxt.readValue(p, java.time.LocalDateTime::class.java)
    return javaLocalDateTime.toKotlinLocalDateTime()
  }
}
