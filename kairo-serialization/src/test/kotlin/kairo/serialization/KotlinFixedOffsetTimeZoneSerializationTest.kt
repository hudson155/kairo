package kairo.serialization

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.FixedOffsetTimeZone
import kotlinx.datetime.UtcOffset
import org.junit.jupiter.api.Test

internal class KotlinFixedOffsetTimeZoneSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(FixedOffsetTimeZone(UtcOffset(-18))).shouldBe("\"-18:00\"")
      json.serialize(FixedOffsetTimeZone(UtcOffset.ZERO)).shouldBe("\"Z\"")
      json.serialize(FixedOffsetTimeZone(UtcOffset(18))).shouldBe("\"+18:00\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<FixedOffsetTimeZone>("\"-18:00\"").shouldBe(FixedOffsetTimeZone(UtcOffset(-18)))
      json.deserialize<FixedOffsetTimeZone>("\"Z\"").shouldBe(FixedOffsetTimeZone(UtcOffset.ZERO))
      json.deserialize<FixedOffsetTimeZone>("\"+18:00\"").shouldBe(FixedOffsetTimeZone(UtcOffset(18)))
    }

  @Test
  fun `deserialize, out of range`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<FixedOffsetTimeZone>("\"-19:00\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.ZoneOffset`" +
          " from String \"-19:00\"",
      )

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<FixedOffsetTimeZone>("\"+19:00\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.ZoneOffset`" +
          " from String \"+19:00\"",
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<FixedOffsetTimeZone>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kotlinx.datetime.FixedOffsetTimeZone(non-null)" +
          " but was null",
      )

      json.deserialize<FixedOffsetTimeZone?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<FixedOffsetTimeZone>("true")
      }.message.shouldStartWith(
        "Unexpected token (VALUE_TRUE)",
      )
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<FixedOffsetTimeZone>("0")
      }.message.shouldStartWith(
        "Unexpected token (VALUE_NUMBER_INT)",
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<FixedOffsetTimeZone>("""{}""")
      }.message.shouldStartWith(
        "Unexpected token (START_OBJECT)",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<FixedOffsetTimeZone>("""[]""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.ZoneOffset`" +
          " from Array value",
      )
    }
}
