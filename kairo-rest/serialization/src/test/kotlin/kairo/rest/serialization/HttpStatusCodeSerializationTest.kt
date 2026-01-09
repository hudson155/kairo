package kairo.rest.serialization

import com.fasterxml.jackson.core.JsonParseException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import io.ktor.http.HttpStatusCode
import kairo.serialization.KairoJson
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class HttpStatusCodeSerializationTest {
  private val json: KairoJson =
    KairoJson {
      addModule(RestModule())
    }

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
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<HttpStatusCode>("\"200\"")
      }.message.shouldStartWith(
        "Current token (VALUE_STRING) not numeric, can not use numeric value accessors",
      )
    }
}
