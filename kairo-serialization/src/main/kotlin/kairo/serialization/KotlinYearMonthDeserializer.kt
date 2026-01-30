package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import kotlinx.datetime.YearMonth
import kotlinx.datetime.toKotlinYearMonth

internal class KotlinYearMonthDeserializer : StdDeserializer<YearMonth>(
  YearMonth::class.java,
) {
  override fun deserialize(
    p: JsonParser,
    ctxt: DeserializationContext,
  ): YearMonth {
    @Suppress("UnnecessaryFullyQualifiedName")
    val javaYearMonth = ctxt.readValue(p, java.time.YearMonth::class.java)
    return javaYearMonth.toKotlinYearMonth()
  }
}
