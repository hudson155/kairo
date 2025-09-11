package kairo.exception

import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Test

internal class LogicalFailureSerializatonTest {
  internal data class Example(
    val key: String?,
  ) : LogicalFailure() {
    override val type: String = "Example"
    override val status: HttpStatusCode = HttpStatusCode.InternalServerError
    override val title: String = "Example"

    override fun JsonObjectBuilder.buildJson() {
      put("key", JsonPrimitive(key))
    }
  }

  @Test
  fun `null key`() {
    Json.encodeToJsonElement(Example(null).json)
      .shouldBe(
        buildJsonObject {
          put("type", JsonPrimitive("Example"))
          put("status", JsonPrimitive(500))
          put("title", JsonPrimitive("Example"))
          put("detail", JsonPrimitive(null))
          put("key", JsonPrimitive(null))
        },
      )
  }

  @Test
  fun `non-null key`() {
    Json.encodeToJsonElement(Example("expected").json)
      .shouldBe(
        buildJsonObject {
          put("type", JsonPrimitive("Example"))
          put("status", JsonPrimitive(500))
          put("title", JsonPrimitive("Example"))
          put("detail", JsonPrimitive(null))
          put("key", JsonPrimitive("expected"))
        },
      )
  }
}
