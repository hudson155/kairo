package kairo.serialization

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import org.junit.jupiter.api.Test

internal class KotlinTimeZoneSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(TimeZone.of("Etc/GMT+12")).shouldBe("\"Etc/GMT+12\"")
      json.serialize(TimeZone.of("UTC")).shouldBe("\"UTC\"")
      json.serialize(TimeZone.of("Pacific/Kiritimati")).shouldBe("\"Pacific/Kiritimati\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<TimeZone>("\"Etc/GMT+12\"").shouldBe(TimeZone.of("Etc/GMT+12"))
      json.deserialize<TimeZone>("\"UTC\"").shouldBe(TimeZone.of("UTC"))
      json.deserialize<TimeZone>("\"Pacific/Kiritimati\"").shouldBe(TimeZone.of("Pacific/Kiritimati"))
    }

  @Test
  fun `deserialize, wrong format (lowercase)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<TimeZone>("\"pacific/kiritimati\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.ZoneId`" +
          " from String \"pacific/kiritimati\"",
      )
    }

  @Test
  fun `deserialize, unknown zone`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<TimeZone>("\"Pacific/Edmonton\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.ZoneId`" +
          " from String \"Pacific/Edmonton\"",
      )
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<TimeZone>("0")
      }.message.shouldStartWith(
        "Unexpected token (VALUE_NUMBER_INT)",
      )
    }
}
