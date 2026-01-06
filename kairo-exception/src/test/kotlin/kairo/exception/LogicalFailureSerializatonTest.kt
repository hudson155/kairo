package kairo.exception

import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import kairo.serialization.KairoJson
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class LogicalFailureSerializatonTest {
  internal data class Example(
    val key: String?,
  ) : LogicalFailure("Example") {
    override val type: String = "Example"
    override val status: HttpStatusCode = HttpStatusCode.InternalServerError

    override fun MutableMap<String, Any?>.buildJson() {
      put("key", key)
    }
  }

  private val json: KairoJson =
    KairoJson {
      pretty = true
    }

  @Test
  fun `null key`(): Unit =
    runTest {
      json.serialize(Example(null).json)
        .shouldBe(
          """
            {
              "type": "Example",
              "status": 500,
              "message": "Example",
              "detail": null,
              "key": null
            }
          """.trimIndent()
        )
    }

  @Test
  fun `non-null key`(): Unit =
    runTest {
      json.serialize(Example("expected").json)
        .shouldBe(
          """
            {
              "type": "Example",
              "status": 500,
              "message": "Example",
              "detail": null,
              "key": "expected"
            }
          """.trimIndent()
        )
    }
}
