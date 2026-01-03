package kairo.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate

internal class KotlinLocalDateSerializer : StdSerializer<LocalDate>(
  LocalDate::class.java,
) {
  override fun serialize(
    value: LocalDate,
    gen: JsonGenerator,
    provider: SerializerProvider,
  ) {
    val javaLocalDate = value.toJavaLocalDate()
    provider.defaultSerializeValue(javaLocalDate, gen)
  }
}
