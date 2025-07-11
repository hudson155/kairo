package kairo.serialization.module

import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kairo.serialization.property.allowUnknownProperties
import kairo.serialization.serializationShouldFail
import kairo.serialization.util.kairoRead
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to unknown property deserialization.
 * Therefore, some test cases are not included since they are not strictly related to unknown properties.
 */
internal class UnknownPropertyObjectMapperTest {
  internal data class MyClass(
    val foo: String,
  )

  @Test
  fun `unknown properties disallowed (default)`(): Unit = runTest {
    val mapper = jsonMapper().build()
    serializationShouldFail {
      mapper.kairoRead<MyClass>("{ \"foo\": \"bar\", \"baz\": \"qux\" }")
    }
  }

  @Test
  fun `unknown properties allowed`(): Unit = runTest {
    val mapper = jsonMapper {
      allowUnknownProperties = true
    }.build()
    mapper.kairoRead<MyClass>("{ \"foo\": \"bar\", \"baz\": \"qux\" }").shouldBe(MyClass("bar"))
  }
}
