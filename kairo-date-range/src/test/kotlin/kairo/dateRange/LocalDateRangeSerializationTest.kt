package kairo.dateRange

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import kairo.serialization.jsonMapper
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class LocalDateRangeSerializationTest {
  private val mapper: JsonMapper = jsonMapper()

  private val typicalRange: LocalDateRange =
    LocalDateRange.inclusive(
      start = LocalDate.parse("2023-11-13"),
      endInclusive = LocalDate.parse("2024-01-04"),
    )

  @Test
  fun serialize(): Unit = runTest {
    mapper.writeValueAsString(typicalRange)
      .shouldBe("{\"start\":\"2023-11-13\",\"endInclusive\":\"2024-01-04\"}")
  }

  @Test
  fun deserialize(): Unit = runTest {
    mapper.readValue<LocalDateRange>("{\"start\":\"2023-11-13\",\"endInclusive\":\"2024-01-04\"}")
      .shouldBe(typicalRange)

    shouldThrow<JsonMappingException> {
      mapper.readValue<LocalDateRange>("{\"start\":\"2023-11-13\",\"endInclusive\":\"2023-11-11\"}")
    }
  }
}
