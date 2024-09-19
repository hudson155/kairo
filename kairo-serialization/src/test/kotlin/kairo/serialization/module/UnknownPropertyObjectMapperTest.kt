package kairo.serialization.module

import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import kairo.serialization.ObjectMapperFactory
import kairo.serialization.ObjectMapperFormat
import kairo.serialization.serializationShouldFail
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
    val mapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"foo\": \"bar\", \"baz\": \"qux\" }")
    }
  }

  @Test
  fun `unknown properties allowed`(): Unit = runTest {
    val mapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json) {
      allowUnknownProperties = true
    }.build()
    mapper.readValue<MyClass>("{ \"foo\": \"bar\", \"baz\": \"qux\" }").shouldBe(MyClass("bar"))
  }
}
