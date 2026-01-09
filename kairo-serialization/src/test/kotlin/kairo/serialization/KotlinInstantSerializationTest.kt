package kairo.serialization

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import java.time.DateTimeException
import kotlin.time.Instant
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.junit.jupiter.api.Test

@Suppress("UnderscoresInNumericLiterals")
internal class KotlinInstantSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(
        LocalDateTime(-2023, Month.JANUARY, 1, 0, 0, 0, 0)
          .toInstant(TimeZone.UTC),
      ).shouldBe("\"-2023-01-01T00:00:00Z\"")
      json.serialize(
        LocalDateTime(2023, Month.NOVEMBER, 14, 22, 13, 20, 123456789)
          .toInstant(TimeZone.UTC),
      ).shouldBe("\"2023-11-14T22:13:20.123456789Z\"")
      json.serialize(
        LocalDateTime(3716, Month.DECEMBER, 30, 23, 59, 59, 999999999)
          .toInstant(TimeZone.UTC),
      ).shouldBe("\"3716-12-30T23:59:59.999999999Z\"")
    }

  @Test
  fun `deserialize, string`(): Unit =
    runTest {
      json.deserialize<Instant>("\"-2023-01-01T00:00:00Z\"")
        .shouldBe(
          LocalDateTime(-2023, Month.JANUARY, 1, 0, 0, 0, 0)
            .toInstant(TimeZone.UTC),
        )
      json.deserialize<Instant>("\"2023-11-14T22:13:20.123456789Z\"")
        .shouldBe(
          LocalDateTime(2023, Month.NOVEMBER, 14, 22, 13, 20, 123456789)
            .toInstant(TimeZone.UTC),
        )
      json.deserialize<Instant>("\"3716-12-30T23:59:59.999999999Z\"")
        .shouldBe(
          LocalDateTime(3716, Month.DECEMBER, 30, 23, 59, 59, 999999999)
            .toInstant(TimeZone.UTC),
        )
    }

  @Test
  fun `deserialize, int`(): Unit =
    runTest {
      json.deserialize<Instant>("1700000000")
        .shouldBe(
          LocalDateTime(2023, Month.NOVEMBER, 14, 22, 13, 20, 0)
            .toInstant(TimeZone.UTC),
        )

      shouldThrowExactly<DateTimeException> {
        json.deserialize<Instant>("1700000000123456789")
      }.message.shouldStartWith(
        "Instant exceeds minimum or maximum instant",
      )
    }

  @Test
  fun `deserialize, float`(): Unit =
    runTest {
      json.deserialize<Instant>("1700000000.0")
        .shouldBe(
          LocalDateTime(2023, Month.NOVEMBER, 14, 22, 13, 20, 0)
            .toInstant(TimeZone.UTC),
        )

      shouldThrowExactly<DateTimeException> {
        json.deserialize<Instant>("1700000000123456789.0")
      }.message.shouldStartWith(
        "Instant exceeds minimum or maximum instant",
      )

      json.deserialize<Instant>("1700000000.123456789")
        .shouldBe(
          LocalDateTime(2023, Month.NOVEMBER, 14, 22, 13, 20, 123456789)
            .toInstant(TimeZone.UTC),
        )
    }

  @Test
  fun `deserialize, month out of range`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Instant>("\"2023-00-14T22:13:20.123456789Z\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Instant`" +
          " from String \"2023-00-14T22:13:20.123456789Z\"",
      )

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Instant>("\"2023-13-14T22:13:20.123456789Z\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Instant`" +
          " from String \"2023-13-14T22:13:20.123456789Z\"",
      )
    }

  @Test
  fun `deserialize, day out of range`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Instant>("\"2023-11-00T22:13:20.123456789Z\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Instant`" +
          " from String \"2023-11-00T22:13:20.123456789Z\"",
      )

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Instant>("\"2023-11-31T22:13:20.123456789Z\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Instant`" +
          " from String \"2023-11-31T22:13:20.123456789Z\"",
      )
    }

  @Test
  fun `deserialize, hour out of range`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Instant>("\"2023-11-14T-01:13:20.123456789Z\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Instant`" +
          " from String \"2023-11-14T-01:13:20.123456789Z\"",
      )

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Instant>("\"2023-11-14T24:13:20.123456789Z\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Instant`" +
          " from String \"2023-11-14T24:13:20.123456789Z\"",
      )
    }

  @Test
  fun `deserialize, minute out of range`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Instant>("\"2023-11-14T22:-01:20.123456789Z\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Instant`" +
          " from String \"2023-11-14T22:-01:20.123456789Z\"",
      )

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Instant>("\"2023-11-14T22:60:20.123456789Z\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Instant`" +
          " from String \"2023-11-14T22:60:20.123456789Z\"",
      )
    }

  @Test
  fun `deserialize, second out of range`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Instant>("\"2023-11-14T22:13:-01.123456789Z\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Instant`" +
          " from String \"2023-11-14T22:13:-01.123456789Z\"",
      )

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Instant>("\"2023-11-14T22:13:60.123456789Z\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Instant`" +
          " from String \"2023-11-14T22:13:60.123456789Z\"",
      )
    }
}
