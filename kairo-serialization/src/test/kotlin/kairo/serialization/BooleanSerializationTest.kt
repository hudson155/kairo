package kairo.serialization

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class BooleanSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(true).shouldBe("true")
      json.serialize(false).shouldBe("false")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<Boolean>("true").shouldBeTrue()
      json.deserialize<Boolean>("false").shouldBeFalse()
    }

  @Test
  fun `deserialize, wrong format (all caps)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Boolean>("TRUE")
      }.message.shouldStartWith(
        "Unrecognized token 'TRUE'",
      )

      shouldThrowExactly<JsonParseException> {
        json.deserialize<Boolean>("FALSE")
      }.message.shouldStartWith(
        "Unrecognized token 'FALSE'",
      )
    }

  @Test
  fun `deserialize, wrong format (leading capital)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Boolean>("True")
      }.message.shouldStartWith(
        "Unrecognized token 'True'",
      )

      shouldThrowExactly<JsonParseException> {
        json.deserialize<Boolean>("False")
      }.message.shouldStartWith(
        "Unrecognized token 'False'",
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<Boolean>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kotlin.Boolean(non-null)" +
          " but was null",
      )

      json.deserialize<Boolean?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Boolean>("-1")
      }.message.shouldStartWith(
        "Cannot coerce Integer value (-1)" +
          " to `java.lang.Boolean` value",
      )

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Boolean>("0")
      }.message.shouldStartWith(
        "Cannot coerce Integer value (0)" +
          " to `java.lang.Boolean` value",
      )

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Boolean>("1")
      }.message.shouldStartWith(
        "Cannot coerce Integer value (1)" +
          " to `java.lang.Boolean` value",
      )
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Boolean>("\"true\"")
      }.message.shouldStartWith(
        "Cannot coerce String value (\"true\")" +
          " to `java.lang.Boolean` value",
      )

      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Boolean>("\"false\"")
      }.message.shouldStartWith(
        "Cannot coerce String value (\"false\")" +
          " to `java.lang.Boolean` value",
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Boolean>("""{}""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.Boolean`" +
          " from Object value",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Boolean>("""[]""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.Boolean`" +
          " from Array value",
      )
    }
}
