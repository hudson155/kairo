package kairo.serialization

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import java.time.ZoneOffset
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class JavaZoneOffsetSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(ZoneOffset.MIN).shouldBe("\"-18:00\"")
      json.serialize(ZoneOffset.UTC).shouldBe("\"Z\"")
      json.serialize(ZoneOffset.MAX).shouldBe("\"+18:00\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<ZoneOffset>("\"-18:00\"").shouldBe(ZoneOffset.MIN)
      json.deserialize<ZoneOffset>("\"Z\"").shouldBe(ZoneOffset.UTC)
      json.deserialize<ZoneOffset>("\"+18:00\"").shouldBe(ZoneOffset.MAX)
    }

  @Test
  fun `deserialize, out of range`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<ZoneOffset>("\"-19:00\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.ZoneOffset`" +
          " from String \"-19:00\""
      )

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<ZoneOffset>("\"+19:00\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.ZoneOffset`" +
          " from String \"+19:00\""
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<ZoneOffset>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified java.time.ZoneOffset(non-null) but was null"
      )

      json.deserialize<ZoneOffset?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<ZoneOffset>("true")
      }.message.shouldStartWith(
        "Unexpected token (VALUE_TRUE)"
      )
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<ZoneOffset>("0")
      }.message.shouldStartWith(
        "Unexpected token (VALUE_NUMBER_INT)"
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<ZoneOffset>("""{}""")
      }.message.shouldStartWith(
        "Unexpected token (START_OBJECT)"
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<ZoneOffset>("""[]""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.ZoneOffset`" +
          " from Array value"
      )
    }
}
