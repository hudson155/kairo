package kairo.serialization

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class SetSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(emptySet<Nothing>()).shouldBe("""[]""")
      json.serialize(setOf(1, 2, 3)).shouldBe("""[1,2,3]""")
      json.serialize(setOf("a", "b", "c")).shouldBe("""["a","b","c"]""")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<Set<Nothing>>("""[]""").shouldBeEmpty()
      json.deserialize<Set<Int>>("""[1,2,3]""").shouldContainExactlyInAnyOrder(1, 2, 3)
      json.deserialize<Set<String>>("""["a","b","c"]""").shouldContainExactlyInAnyOrder("a", "b", "c")
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<Set<Nothing>>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kotlin.collections.Set(non-null)" +
          " but was null",
      )

      json.deserialize<Set<Nothing>?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong element type`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Set<Int>>("""["a"]""")
      }.message.shouldStartWith(
        "Cannot coerce String value (\"a\")" +
          " to `java.lang.Integer` value",
      )
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Set<Int>>("0")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.util.HashSet<java.lang.Integer>`" +
          " from Integer value",
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Set<Nothing>>("""{}""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.util.HashSet<java.lang.Object>`" +
          " from Object value",
      )
    }
}
