package kairo.serialization

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

/**
 * This test is intended to test behaviour strictly related to string serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to strings.
 */
internal class StringObjectMapperTest : FunSpec({
  val mapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  context("serialize") {
    context("default") {
      test("empty") {
        mapper.writeValueAsString(MyClass("")).shouldBe("{\"value\":\"\"}")
      }
      test("non-empty") {
        mapper.writeValueAsString(MyClass("true")).shouldBe("{\"value\":\"true\"}")
      }
    }
    context("nullable") {
      test("empty") {
        mapper.writeValueAsString(MyClassNullable("")).shouldBe("{\"value\":\"\"}")
      }
      test("non-empty") {
        mapper.writeValueAsString(MyClassNullable("true")).shouldBe("{\"value\":\"true\"}")
      }
      test("null") {
        mapper.writeValueAsString(MyClassNullable(null)).shouldBe("{\"value\":null}")
      }
    }
  }

  context("deserialize") {
    context("default") {
      test("empty") {
        mapper.readValue<MyClass>("{ \"value\": \"\" }").shouldBe(MyClass(""))
      }
      test("non-empty") {
        mapper.readValue<MyClass>("{ \"value\": \"true\" }").shouldBe(MyClass("true"))
      }
      test("null") {
        shouldThrow<MismatchedInputException> {
          mapper.readValue<MyClass>("{ \"value\": null }")
        }
      }
      test("missing") {
        shouldThrow<MismatchedInputException> {
          mapper.readValue<MyClass>("{}")
        }
      }
    }
    context("nullable") {
      test("empty") {
        mapper.readValue<MyClassNullable>("{ \"value\": \"\" }").shouldBe(MyClassNullable(""))
      }
      test("non-empty") {
        mapper.readValue<MyClassNullable>("{ \"value\": \"true\" }").shouldBe(MyClassNullable("true"))
      }
      test("null") {
        mapper.readValue<MyClassNullable>("{ \"value\": null }").shouldBe(MyClassNullable(null))
      }
      test("missing") {
        mapper.readValue<MyClassNullable>("{}").shouldBe(MyClassNullable(null))
      }
    }
    context("wrong type") {
      test("number") {
        shouldThrow<MismatchedInputException> {
          mapper.readValue<MyClass>("{ \"value\": 0 }")
        }
      }
      test("float") {
        shouldThrow<MismatchedInputException> {
          mapper.readValue<MyClass>("{ \"value\": 1.0 }")
        }
      }
      test("boolean") {
        shouldThrow<MismatchedInputException> {
          mapper.readValue<MyClass>("{ \"value\": true }")
        }
      }
    }
  }
}) {
  private data class MyClass(
    val value: String,
  )

  private data class MyClassNullable(
    val value: String?,
  )
}
