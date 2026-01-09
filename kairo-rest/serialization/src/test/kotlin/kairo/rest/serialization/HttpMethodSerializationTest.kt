package kairo.rest.serialization

import io.kotest.matchers.shouldBe
import io.ktor.http.HttpMethod
import kairo.serialization.KairoJson
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class HttpMethodSerializationTest {
  private val json: KairoJson =
    KairoJson {
      addModule(RestModule())
    }

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
}
