package kairo.serialization

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

/**
 * This test is intended to test behaviour strictly related to unknown property deserialization.
 * Therefore, some test cases are not included since they are not strictly related to unknown properties.
 */
internal class UnknownPropertyObjectMapperTest : FunSpec({
  test("disallowed (default)") {
    val mapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()
    shouldThrow<UnrecognizedPropertyException> {
      mapper.readValue<MyClass>("{ \"foo\": \"bar\", \"baz\": \"qux\" }")
    }
  }
  test("allowed") {
    val mapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json) {
      allowUnknownProperties = true
    }.build()
    mapper.readValue<MyClass>("{ \"foo\": \"bar\", \"baz\": \"qux\" }").shouldBe(MyClass("bar"))
  }
}) {
  internal data class MyClass(
    val foo: String,
  )
}
