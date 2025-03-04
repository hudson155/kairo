package kairo.dateRange

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.json.JsonMapper
import io.github.projectmapk.jackson.module.kogera.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import java.time.Instant
import kairo.serialization.jsonMapper
import kairo.serialization.util.kairoWrite
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class InstantRangeSerializationTest {
  private val mapper: JsonMapper = jsonMapper().build()

  private val typicalRange: InstantRange =
    InstantRange.exclusive(
      start = Instant.parse("2023-11-13T19:44:32.123456789Z"),
      endExclusive = Instant.parse("2024-01-04T00:01:59.567890123Z"),
    )

  @Test
  fun serialize(): Unit = runTest {
    mapper.kairoWrite(typicalRange)
      .shouldBe(
        "{\"start\":\"2023-11-13T19:44:32.123456789Z\",\"endExclusive\":\"2024-01-04T00:01:59.567890123Z\"}",
      )
  }

  @Test
  fun deserialize(): Unit = runTest {
    mapper.readValue<InstantRange>(
      "{\"start\":\"2023-11-13T19:44:32.123456789Z\",\"endExclusive\":\"2024-01-04T00:01:59.567890123Z\"}",
    ).shouldBe(typicalRange)

    shouldThrow<JsonMappingException> {
      mapper.readValue<InstantRange>(
        "{\"start\":\"2023-11-13T19:44:32.123456789Z\",\"endExclusive\":\"2023-11-13T19:44:32.123456788Z\"}",
      )
    }
  }
}
