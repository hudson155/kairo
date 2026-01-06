package kairo.serialization

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import java.time.ZoneId
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
      json.deserialize<ZoneId>("\"Etc/GMT+12\"").shouldBe(ZoneId.of("Etc/GMT+12"))
      json.deserialize<ZoneId>("\"UTC\"").shouldBe(ZoneId.of("UTC"))
      json.deserialize<ZoneId>("\"Pacific/Kiritimati\"").shouldBe(ZoneId.of("Pacific/Kiritimati"))
    }

  @Test
  fun `deserialize, wrong format (lowercase)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<ZoneId>("\"pacific/kiritimati\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.ZoneId`" +
          " from String \"pacific/kiritimati\"",
      )
    }

  @Test
  fun `deserialize, unknown`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<ZoneId>("\"Pacific/Edmonton\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.ZoneId`" +
          " from String \"Pacific/Edmonton\"",
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<ZoneId>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified java.time.ZoneId(non-null)" +
          " but was null",
      )

      json.deserialize<ZoneId?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<ZoneId>("true")
      }.message.shouldStartWith(
        "Unexpected token (VALUE_TRUE)",
      )
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<ZoneId>("0")
      }.message.shouldStartWith(
        "Unexpected token (VALUE_NUMBER_INT)",
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<ZoneId>("""{}""")
      }.message.shouldStartWith(
        "Unexpected token (START_OBJECT)",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<ZoneId>("""[]""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.ZoneId`" +
          " from Array value",
      )
    }
}
