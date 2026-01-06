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

internal class HttpStatusCodeAsObjectSerializationTest {
  internal data class DefaultWrapper(
    val value: HttpStatusCode,
  )

  internal data class WrapperWithAnnotations(
    @JsonSerialize(using = HttpStatusCodeSerializer.AsObject::class)
    @JsonDeserialize(using = HttpStatusCodeDeserializer.AsObject::class)
    val value: HttpStatusCode,
  )

  private val json: KairoJson = KairoJson()

  @Test
  fun `serialize, configured in constructor`(): Unit =
    runTest {
      val json = KairoJson {
        httpStatusCodeFormat = HttpStatusCodeFormat.Object
      }

      json.serialize(DefaultWrapper(HttpStatusCode.OK))
        .shouldBe("""{"value":{"value":200,"description":"OK"}}""")
      json.serialize(DefaultWrapper(HttpStatusCode.ServiceUnavailable))
        .shouldBe("""{"value":{"value":503,"description":"Service Unavailable"}}""")
      json.serialize(DefaultWrapper(HttpStatusCode.fromValue(999)))
        .shouldBe("""{"value":{"value":999,"description":"Unknown Status Code"}}""")
    }

  @Test
  fun `serialize, configured using annotations`(): Unit =
    runTest {
      json.serialize(WrapperWithAnnotations(HttpStatusCode.OK))
        .shouldBe("""{"value":{"value":200,"description":"OK"}}""")
      json.serialize(WrapperWithAnnotations(HttpStatusCode.ServiceUnavailable))
        .shouldBe("""{"value":{"value":503,"description":"Service Unavailable"}}""")
      json.serialize(WrapperWithAnnotations(HttpStatusCode.fromValue(999)))
        .shouldBe("""{"value":{"value":999,"description":"Unknown Status Code"}}""")
    }

  @Test
  fun `deserialize, configured in constructor`(): Unit =
    runTest {
      val json = KairoJson {
        httpStatusCodeFormat = HttpStatusCodeFormat.Object
      }

      json.deserialize<DefaultWrapper>("""{"value":{"value":200,"description":"OK"}}""")
        .shouldBe(DefaultWrapper(HttpStatusCode.OK))
      json.deserialize<DefaultWrapper>("""{"value":{"value":503,"description":"Service Unavailable"}}""")
        .shouldBe(DefaultWrapper(HttpStatusCode.ServiceUnavailable))
      json.deserialize<DefaultWrapper>("""{"value":{"value":999,"description":"Unknown Status Code"}}""")
        .shouldBe(DefaultWrapper(HttpStatusCode.fromValue(999)))
    }

  @Test
  fun `deserialize, configured using annotations`(): Unit =
    runTest {
      json.deserialize<WrapperWithAnnotations>("""{"value":{"value":200,"description":"OK"}}""")
        .shouldBe(WrapperWithAnnotations(HttpStatusCode.OK))
      json.deserialize<WrapperWithAnnotations>("""{"value":{"value":503,"description":"Service Unavailable"}}""")
        .shouldBe(WrapperWithAnnotations(HttpStatusCode.ServiceUnavailable))
      json.deserialize<WrapperWithAnnotations>("""{"value":{"value":999,"description":"Unknown Status Code"}}""")
        .shouldBe(WrapperWithAnnotations(HttpStatusCode.fromValue(999)))
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<DefaultWrapper>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kairo.serialization.HttpStatusCodeAsObjectSerializationTest.DefaultWrapper(non-null)" +
          " but was null",
      )

      json.deserialize<DefaultWrapper?>("null").shouldBeNull()
    }
}
