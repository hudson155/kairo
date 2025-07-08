package kairo.serialization.module.ktor

import com.fasterxml.jackson.databind.json.JsonMapper
import io.kotest.matchers.shouldBe
import io.ktor.http.ContentType
import kairo.serialization.jsonMapper
import kairo.serialization.serializationShouldFail
import kairo.serialization.util.kairoRead
import kairo.serialization.util.kairoWrite
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to content type serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to content types.
 */
internal class ContentTypeDefaultObjectMapperTest {
  /**
   * This test is specifically for non-nullable properties.
   */
  internal data class MyClass(
    val value: ContentType,
  )

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun serialize(): Unit = runTest {
    mapper.kairoWrite(MyClass(ContentType.Application.Json))
      .shouldBe("{\"value\":\"application/json\"}")
  }

  @Test
  fun deserialize(): Unit = runTest {
    mapper.kairoRead<MyClass>("{ \"value\": \"application/json\" }")
      .shouldBe(MyClass(ContentType.Application.Json))
  }

  @Test
  fun `deserialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.kairoRead<MyClass>("{ \"value\": null }")
    }
  }

  @Test
  fun `deserialize, missing`(): Unit = runTest {
    serializationShouldFail {
      mapper.kairoRead<MyClass>("{}")
    }
  }

  @Test
  fun `deserialize, no slash`(): Unit = runTest {
    serializationShouldFail {
      mapper.kairoRead<MyClass>("{ \"value\": \"applicationjson\" }")
    }
  }

  @Test
  fun `deserialize, two slashes`(): Unit = runTest {
    serializationShouldFail {
      mapper.kairoRead<MyClass>("{ \"value\": \"application/json/json\" }")
    }
  }
}
