package kairo.serialization

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import io.ktor.http.HttpMethod
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class HttpMethodSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(HttpMethod.Get).shouldBe("\"GET\"")
      json.serialize(HttpMethod.Post).shouldBe("\"POST\"")
      json.serialize(HttpMethod("CUSTOM")).shouldBe("\"CUSTOM\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<HttpMethod>("\"GET\"").shouldBe(HttpMethod.Get)
      json.deserialize<HttpMethod>("\"POST\"").shouldBe(HttpMethod.Post)
      json.deserialize<HttpMethod>("\"CUSTOM\"").shouldBe(HttpMethod("CUSTOM"))
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<HttpMethod>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified io.ktor.http.HttpMethod(non-null)" +
          " but was null",
      )

      json.deserialize<HttpMethod?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<HttpMethod>("""{}""")
      }.message.shouldStartWith(
        "Trailing token (of type END_OBJECT) found after value" +
          " (bound as `io.ktor.http.HttpMethod`)",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<HttpMethod>("""[]""")
      }.message.shouldStartWith(
        "Trailing token (of type END_ARRAY) found after value" +
          " (bound as `io.ktor.http.HttpMethod`)",
      )
    }
}
