package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toKotlinLocalTime

internal class KotlinLocalTimeDeserializer : StdDeserializer<LocalTime>(
  LocalTime::class.java,
) {
  override fun deserialize(
    p: JsonParser,
    ctxt: DeserializationContext,
  ): LocalTime {
    val javaLocalTime = ctxt.readValue(p, java.time.LocalTime::class.java)
    return javaLocalTime.toKotlinLocalTime()
  }
}
