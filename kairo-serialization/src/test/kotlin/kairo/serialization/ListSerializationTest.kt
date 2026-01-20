package kairo.serialization

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class ListSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(emptyList<Nothing>()).shouldBe("""[]""")
      json.serialize(listOf(1, 2, 3)).shouldBe("""[1,2,3]""")
      json.serialize(listOf("a", "b", "c")).shouldBe("""["a","b","c"]""")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<List<Nothing>>("""[]""").shouldBeEmpty()
      json.deserialize<List<Int>>("""[1,2,3]""").shouldContainExactly(1, 2, 3)
      json.deserialize<List<String>>("""["a","b","c"]""").shouldContainExactly("a", "b", "c")
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<List<Nothing>>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kotlin.collections.List(non-null)" +
          " but was null",
      )

      json.deserialize<List<Nothing>?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong element type`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<List<Int>>("""["a"]""")
      }.message.shouldStartWith(
        "Cannot coerce String value (\"a\")" +
          " to `java.lang.Integer` value",
      )
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<List<Int>>("0")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.util.ArrayList<java.lang.Integer>`" +
          " from Integer value",
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<List<Nothing>>("""{}""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.util.ArrayList<java.lang.Void>`" +
          " from Object value",
      )
    }
}
