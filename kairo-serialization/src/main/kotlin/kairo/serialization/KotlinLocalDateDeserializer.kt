package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate

internal class KotlinLocalDateDeserializer : StdDeserializer<LocalDate>(
  LocalDate::class.java
) {
  override fun deserialize(
    p: JsonParser,
    ctxt: DeserializationContext,
  ): LocalDate {
    val javaLocalDate = ctxt.readValue(p, java.time.LocalDate::class.java)
    return javaLocalDate.toKotlinLocalDate()
  }
}
