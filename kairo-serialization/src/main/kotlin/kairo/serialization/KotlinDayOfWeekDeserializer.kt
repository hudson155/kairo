package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.toKotlinDayOfWeek

internal class KotlinDayOfWeekDeserializer : StdDeserializer<DayOfWeek>(
  DayOfWeek::class.java,
) {
  override fun deserialize(
    p: JsonParser,
    ctxt: DeserializationContext,
  ): DayOfWeek {
    @Suppress("UnnecessaryFullyQualifiedName")
    val javaDayOfWeek = ctxt.readValue(p, java.time.DayOfWeek::class.java)
    return javaDayOfWeek.toKotlinDayOfWeek()
  }
}
