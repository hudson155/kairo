package kairo.serialization

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class HttpStatusCodeAsNumberSerializationTest {
  internal data class DefaultWrapper(
    val value: HttpStatusCode,
  )

  internal data class WrapperWithAnnotations(
    @JsonSerialize(using = HttpStatusCodeSerializer.AsNumber::class)
    @JsonDeserialize(using = HttpStatusCodeDeserializer.AsNumber::class)
    val value: HttpStatusCode,
  )

  private val json: KairoJson = KairoJson()

  @Test
  fun `serialize, configured in constructor`(): Unit =
    runTest {
      json.serialize(DefaultWrapper(HttpStatusCode.OK))
        .shouldBe("""{"value":200}""")
      json.serialize(DefaultWrapper(HttpStatusCode.ServiceUnavailable))
        .shouldBe("""{"value":503}""")
      json.serialize(DefaultWrapper(HttpStatusCode.fromValue(999)))
        .shouldBe("""{"value":999}""")
    }

  @Test
  fun `serialize, configured using annotations`(): Unit =
    runTest {
      json.serialize(WrapperWithAnnotations(HttpStatusCode.OK))
        .shouldBe("""{"value":200}""")
      json.serialize(WrapperWithAnnotations(HttpStatusCode.ServiceUnavailable))
        .shouldBe("""{"value":503}""")
      json.serialize(WrapperWithAnnotations(HttpStatusCode.fromValue(999)))
        .shouldBe("""{"value":999}""")
    }

  @Test
  fun `deserialize, configured in constructor`(): Unit =
    runTest {
      json.deserialize<DefaultWrapper>("""{"value":200}""")
        .shouldBe(DefaultWrapper(HttpStatusCode.OK))
      json.deserialize<DefaultWrapper>("""{"value":503}""")
        .shouldBe(DefaultWrapper(HttpStatusCode.ServiceUnavailable))
      json.deserialize<DefaultWrapper>("""{"value":999}""")
        .shouldBe(DefaultWrapper(HttpStatusCode.fromValue(999)))
    }

  @Test
  fun `deserialize, configured using annotations`(): Unit =
    runTest {
      json.deserialize<WrapperWithAnnotations>("""{"value":200}""")
        .shouldBe(WrapperWithAnnotations(HttpStatusCode.OK))
      json.deserialize<WrapperWithAnnotations>("""{"value":503}""")
        .shouldBe(WrapperWithAnnotations(HttpStatusCode.ServiceUnavailable))
      json.deserialize<WrapperWithAnnotations>("""{"value":999}""")
        .shouldBe(WrapperWithAnnotations(HttpStatusCode.fromValue(999)))
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<DefaultWrapper>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kairo.serialization.HttpStatusCodeAsNumberSerializationTest.DefaultWrapper(non-null)" +
          " but was null",
      )

      json.deserialize<DefaultWrapper?>("null").shouldBeNull()
    }
}
