package kairo.serialization

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalTime
import org.junit.jupiter.api.Test

@Suppress("UnderscoresInNumericLiterals")
internal class KotlinLocalTimeSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(LocalTime(0, 0, 0, 0))
        .shouldBe("\"00:00:00\"")
      json.serialize(LocalTime(22, 13, 20, 123456789))
        .shouldBe("\"22:13:20.123456789\"")
      json.serialize(LocalTime(23, 59, 59, 999999999))
        .shouldBe("\"23:59:59.999999999\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<LocalTime>("\"00:00:00\"")
        .shouldBe(LocalTime(0, 0, 0, 0))
      json.deserialize<LocalTime>("\"22:13:20.123456789\"")
        .shouldBe(LocalTime(22, 13, 20, 123456789))
      json.deserialize<LocalTime>("\"23:59:59.999999999\"")
        .shouldBe(LocalTime(23, 59, 59, 999999999))
    }

  @Test
  fun `deserialize, hour out of range`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<LocalTime>("\"-01:13:20.123456789\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.LocalTime`" +
          " from String \"-01:13:20.123456789\""
      )

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<LocalTime>("\"24:13:20.123456789\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.LocalTime`" +
          " from String \"24:13:20.123456789\""
      )
    }

  @Test
  fun `deserialize, minute out of range`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<LocalTime>("\"22:-01:20.123456789\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.LocalTime`" +
          " from String \"22:-01:20.123456789\""
      )

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<LocalTime>("\"22:60:20.123456789\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.LocalTime`" +
          " from String \"22:60:20.123456789\""
      )
    }

  @Test
  fun `deserialize, second out of range`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<LocalTime>("\"22:13:-01.123456789\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.LocalTime`" +
          " from String \"22:13:-01.123456789\""
      )

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<LocalTime>("\"22:13:60.123456789\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.LocalTime`" +
          " from String \"22:13:60.123456789\""
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<LocalTime>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kotlinx.datetime.LocalTime(non-null) but was null"
      )

      json.deserialize<LocalTime?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<LocalTime>("true")
      }.message.shouldStartWith(
        "Expected array or string"
      )
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<LocalTime>("221320")
      }.message.shouldStartWith(
        "raw timestamp (221320) not allowed for `java.time.LocalTime`"
      )

      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<LocalTime>("221320123456789")
      }.message.shouldStartWith(
        "raw timestamp (221320123456789) not allowed for `java.time.LocalTime`"
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<LocalTime>("""{}""")
      }.message.shouldStartWith(
        "Expected array or string"
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<NullPointerException> {
        json.deserialize<LocalTime>("""[]""")
      }.message.shouldBeNull() // This seems like a bug in Jackson! I don't think this should be null.
    }
}
