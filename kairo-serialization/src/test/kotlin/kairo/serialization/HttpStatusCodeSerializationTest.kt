package kairo.serialization

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class HttpStatusCodeSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(HttpStatusCode.OK).shouldBe("200")
      json.serialize(HttpStatusCode.ServiceUnavailable).shouldBe("503")
      json.serialize(HttpStatusCode.fromValue(999)).shouldBe("999")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<HttpStatusCode>("200").shouldBe(HttpStatusCode.OK)
      json.deserialize<HttpStatusCode>("503").shouldBe(HttpStatusCode.ServiceUnavailable)
      json.deserialize<HttpStatusCode>("999").shouldBe(HttpStatusCode.fromValue(999))
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<HttpStatusCode>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified io.ktor.http.HttpStatusCode(non-null)" +
          " but was null",
      )

      json.deserialize<HttpStatusCode?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<HttpStatusCode>("true")
      }.message.shouldStartWith(
        "Current token (VALUE_TRUE) not numeric, can not use numeric value accessors",
      )
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<HttpStatusCode>("\"0\"")
      }.message.shouldStartWith(
        "Current token (VALUE_STRING) not numeric, can not use numeric value accessors",
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<HttpStatusCode>("""{}""")
      }.message.shouldStartWith(
        "Current token (START_OBJECT) not numeric, can not use numeric value accessors",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<HttpStatusCode>("""[]""")
      }.message.shouldStartWith(
        "Current token (START_ARRAY) not numeric, can not use numeric value accessors",
      )
    }
}
