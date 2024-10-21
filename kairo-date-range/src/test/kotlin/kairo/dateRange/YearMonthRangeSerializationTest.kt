package kairo.dateRange

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import java.time.YearMonth
import kairo.serialization.ObjectMapperFactory
import kairo.serialization.ObjectMapperFormat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class YearMonthRangeSerializationTest {
  private val mapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  private val typicalRange: YearMonthRange =
    YearMonthRange.inclusive(
      start = YearMonth.parse("2022-07"),
      endInclusive = YearMonth.parse("2024-03"),
    )

  @Test
  fun serialize(): Unit = runTest {
    mapper.writeValueAsString(typicalRange)
      .shouldBe("{\"start\":\"2022-07\",\"endInclusive\":\"2024-03\"}")
  }

  @Test
  fun deserialize(): Unit = runTest {
    mapper.readValue<YearMonthRange>("{\"start\":\"2022-07\",\"endInclusive\":\"2024-03\"}")
      .shouldBe(typicalRange)

    shouldThrow<JsonMappingException> {
      mapper.readValue<YearMonthRange>("{\"start\":\"2022-07\",\"endInclusive\":\"2022-05\"}")
    }
  }
}
