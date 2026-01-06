package kairo.serialization

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.Month
import java.time.OffsetDateTime
import java.time.ZoneOffset
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@Suppress("UnderscoresInNumericLiterals")
internal class JavaOffsetDateTimeSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(
        LocalDateTime.of(-2023, Month.JANUARY, 1, 0, 0, 0, 0)
          .atOffset(ZoneOffset.MIN),
      ).shouldBe("\"-2023-01-01T00:00:00-18:00\"")
      json.serialize(
        LocalDateTime.of(2023, Month.NOVEMBER, 14, 22, 13, 20, 123456789)
          .atOffset(ZoneOffset.UTC),
      ).shouldBe("\"2023-11-14T22:13:20.123456789Z\"")
      json.serialize(
        LocalDateTime.of(3716, Month.DECEMBER, 30, 23, 59, 59, 999999999)
          .atOffset(ZoneOffset.MAX),
      ).shouldBe("\"3716-12-30T23:59:59.999999999+18:00\"")
    }

  @Test
  fun `deserialize, string`(): Unit =
    runTest {
      json.deserialize<OffsetDateTime>("\"-2023-01-01T00:00:00-18:00\"")
        .shouldBe(
          LocalDateTime.of(-2023, Month.JANUARY, 1, 0, 0, 0, 0)
            .atOffset(ZoneOffset.MIN),
        )
      json.deserialize<OffsetDateTime>("\"2023-11-14T22:13:20.123456789Z\"")
        .shouldBe(
          LocalDateTime.of(2023, Month.NOVEMBER, 14, 22, 13, 20, 123456789)
            .atOffset(ZoneOffset.UTC),
        )
      json.deserialize<OffsetDateTime>("\"3716-12-30T23:59:59.999999999+18:00\"")
        .shouldBe(
          LocalDateTime.of(3716, Month.DECEMBER, 30, 23, 59, 59, 999999999)
            .atOffset(ZoneOffset.MAX),
        )
    }

  @Test
  fun `deserialize, int`(): Unit =
    runTest {
      json.deserialize<OffsetDateTime>("1700000000")
        .shouldBe(
          LocalDateTime.of(2023, Month.NOVEMBER, 14, 22, 13, 20, 0)
            .atOffset(ZoneOffset.UTC),
        )

      shouldThrowExactly<DateTimeException> {
        json.deserialize<OffsetDateTime>("1700000000123456789")
      }.message.shouldStartWith(
        "Instant exceeds minimum or maximum instant",
      )
    }

  @Test
  fun `deserialize, float`(): Unit =
    runTest {
      json.deserialize<OffsetDateTime>("1700000000.0")
        .shouldBe(
          LocalDateTime.of(2023, Month.NOVEMBER, 14, 22, 13, 20, 0)
            .atOffset(ZoneOffset.UTC),
        )

      shouldThrowExactly<DateTimeException> {
        json.deserialize<OffsetDateTime>("1700000000123456789.0")
      }.message.shouldStartWith(
        "Instant exceeds minimum or maximum instant",
      )

      json.deserialize<OffsetDateTime>("1700000000.123456789")
        .shouldBe(
          LocalDateTime.of(2023, Month.NOVEMBER, 14, 22, 13, 20, 123456789)
            .atOffset(ZoneOffset.UTC),
        )
    }

  @Test
  fun `deserialize, month out of range`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<OffsetDateTime>("\"2023-00-14T22:13:20.123456789Z\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.OffsetDateTime`" +
          " from String \"2023-00-14T22:13:20.123456789Z\"",
      )

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<OffsetDateTime>("\"2023-13-14T22:13:20.123456789Z\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.OffsetDateTime`" +
          " from String \"2023-13-14T22:13:20.123456789Z\"",
      )
    }

  @Test
  fun `deserialize, day out of range`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<OffsetDateTime>("\"2023-11-00T22:13:20.123456789Z\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.OffsetDateTime`" +
          " from String \"2023-11-00T22:13:20.123456789Z\"",
      )

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<OffsetDateTime>("\"2023-11-31T22:13:20.123456789Z\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.OffsetDateTime`" +
          " from String \"2023-11-31T22:13:20.123456789Z\"",
      )
    }

  @Test
  fun `deserialize, hour out of range`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<OffsetDateTime>("\"2023-11-14T-01:13:20.123456789Z\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.OffsetDateTime`" +
          " from String \"2023-11-14T-01:13:20.123456789Z\"",
      )

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<OffsetDateTime>("\"2023-11-14T24:13:20.123456789Z\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.OffsetDateTime`" +
          " from String \"2023-11-14T24:13:20.123456789Z\"",
      )
    }

  @Test
  fun `deserialize, minute out of range`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<OffsetDateTime>("\"2023-11-14T22:-01:20.123456789Z\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.OffsetDateTime`" +
          " from String \"2023-11-14T22:-01:20.123456789Z\"",
      )

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<OffsetDateTime>("\"2023-11-14T22:60:20.123456789Z\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.OffsetDateTime`" +
          " from String \"2023-11-14T22:60:20.123456789Z\"",
      )
    }

  @Test
  fun `deserialize, second out of range`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<OffsetDateTime>("\"2023-11-14T22:13:-01.123456789Z\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.OffsetDateTime`" +
          " from String \"2023-11-14T22:13:-01.123456789Z\"",
      )

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<OffsetDateTime>("\"2023-11-14T22:13:60.123456789Z\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.OffsetDateTime`" +
          " from String \"2023-11-14T22:13:60.123456789Z\"",
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<OffsetDateTime>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified java.time.OffsetDateTime(non-null) but was null",
      )

      json.deserialize<OffsetDateTime?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<OffsetDateTime>("true")
      }.message.shouldStartWith(
        "Unexpected token (VALUE_TRUE)",
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<OffsetDateTime>("""{}""")
      }.message.shouldStartWith(
        "Unexpected token (START_OBJECT)",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<OffsetDateTime>("""[]""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.OffsetDateTime`" +
          " from Array value",
      )
    }
}
