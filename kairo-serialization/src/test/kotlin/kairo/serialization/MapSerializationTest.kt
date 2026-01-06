package kairo.serialization

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class MapSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(emptyMap<String, Nothing>())
        .shouldBe("""{}""")
      json.serialize(mapOf("first" to 1, "second" to 2))
        .shouldBe("""{"first":1,"second":2}""")
      json.serialize(mapOf("first" to "foo", "second" to "bar"))
        .shouldBe("""{"first":"foo","second":"bar"}""")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<Map<String, Nothing>>("""{}""")
        .shouldBeEmpty()
      json.deserialize<Map<String, Int>>("""{"first":1,"second":2}""")
        .shouldContainExactly(mapOf("first" to 1, "second" to 2))
      json.deserialize<Map<String, String>>("""{"first":"foo","second":"bar"}""")
        .shouldContainExactly(mapOf("first" to "foo", "second" to "bar"))
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<Map<String, Nothing>>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kotlin.collections.Map(non-null) but was null",
      )

      json.deserialize<Map<String, Nothing>?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong key type`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Map<String, Int>>("""{0:1}""")
      }.message.shouldStartWith(
        "Unexpected character ('0' (code 48))" +
          ": was expecting double-quote to start field name",
      )
    }

  @Test
  fun `deserialize, wrong value type`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Map<String, Int>>("""{"first":"foo"}""")
      }.message.shouldStartWith(
        "Cannot coerce String value (\"foo\")" +
          " to `java.lang.Integer` value",
      )
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Map<String, Nothing>>("true")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.util.LinkedHashMap<java.lang.Object,java.lang.Object>`" +
          " from Boolean value",
      )
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Map<String, Nothing>>("0")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.util.LinkedHashMap<java.lang.Object,java.lang.Object>`" +
          " from Integer value",
      )
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Map<String, Nothing>>("\"0\"")
      }.message.shouldStartWith(
        "Cannot construct instance of `java.util.LinkedHashMap`",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Map<String, Nothing>>("""[]""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.util.LinkedHashMap<java.lang.Object,java.lang.Object>`" +
          " from Array value",
      )
    }
}
