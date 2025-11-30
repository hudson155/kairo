package kairo.optional

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kairo.serialization.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Contextual
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.modules.plus
import org.junit.jupiter.api.Test

internal class RequiredSerializationTest {
  @Serializable
  internal data class Wrapper(
    @EncodeDefault(EncodeDefault.Mode.NEVER) @Contextual
    val value: Required<String> = Required.Missing,
  )

  private val json: Json = json { serializersModule += optionalModule() }

  @Test
  fun `serialize, missing`(): Unit =
    runTest {
      json.encodeToJsonElement(Wrapper(Required.Missing))
        .shouldBe(buildJsonObject {})
    }

  @Test
  fun `serialize, present`(): Unit =
    runTest {
      json.encodeToJsonElement(Wrapper(Required.Value("some value")))
        .shouldBe(
          buildJsonObject {
            put("value", JsonPrimitive("some value"))
          },
        )
    }

  @Test
  fun `deserialize, missing`(): Unit =
    runTest {
      json.decodeFromString<Wrapper>("{}")
        .shouldBe(Wrapper(Required.Missing))
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrow<SerializationException> {
        json.decodeFromString<Wrapper>("""{"value":null}""")
      }
    }

  @Test
  fun `deserialize, present`(): Unit =
    runTest {
      json.decodeFromString<Wrapper>("""{"value":"some value"}""")
        .shouldBe(Wrapper(Required.Value("some value")))
    }
}
