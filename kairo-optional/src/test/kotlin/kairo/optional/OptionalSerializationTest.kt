package kairo.optional

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Contextual
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.modules.plus
import org.junit.jupiter.api.Test

internal class OptionalSerializationTest {
  @Serializable
  internal data class Wrapper(
    @EncodeDefault(EncodeDefault.Mode.NEVER) @Contextual
    val value: Optional<String> = Optional.Missing,
  )

  private val json: Json = Json { serializersModule += optionalModule }

  @Test
  fun `deserialize, missing`(): Unit =
    runTest {
      json.decodeFromString<Wrapper>("{}")
        .shouldBe(Wrapper(Optional.Missing))
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      json.decodeFromString<Wrapper>("""{"value":null}""")
        .shouldBe(Wrapper(Optional.Null))
    }

  @Test
  fun `deserialize, present`(): Unit =
    runTest {
      json.decodeFromString<Wrapper>("""{"value":"some value"}""")
        .shouldBe(Wrapper(Optional.Value("some value")))
    }

  @Test
  fun `serialize, missing`(): Unit =
    runTest {
      json.encodeToJsonElement(Wrapper(Optional.Missing))
        .shouldBe(buildJsonObject {})
    }

  @Test
  fun `serialize, null`(): Unit =
    runTest {
      json.encodeToJsonElement(Wrapper(Optional.Null))
        .shouldBe(
          buildJsonObject {
            put("value", JsonPrimitive(null))
          },
        )
    }

  @Test
  fun `serialize, present`(): Unit =
    runTest {
      json.encodeToJsonElement(Wrapper(Optional.Value("some value")))
        .shouldBe(
          buildJsonObject {
            put("value", JsonPrimitive("some value"))
          },
        )
    }
}
