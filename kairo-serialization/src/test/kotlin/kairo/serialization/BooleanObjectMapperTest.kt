package kairo.serialization

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

/**
 * This test is intended to test behaviour strictly related to boolean serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to booleans.
 */
internal class BooleanObjectMapperTest : FunSpec({
  val mapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  context("serialize") {
    context("default") {
      test("false") {
        mapper.writeValueAsString(MyClass(false)).shouldBe("{\"value\":false}")
      }
      test("true") {
        mapper.writeValueAsString(MyClass(true)).shouldBe("{\"value\":true}")
      }
    }
    context("is") {
      test("false") {
        mapper.writeValueAsString(MyClassIs(false)).shouldBe("{\"isValue\":false}")
      }
      test("true") {
        mapper.writeValueAsString(MyClassIs(true)).shouldBe("{\"isValue\":true}")
      }
    }
    context("nullable") {
      test("false") {
        mapper.writeValueAsString(MyClassNullable(false)).shouldBe("{\"value\":false}")
      }
      test("true") {
        mapper.writeValueAsString(MyClassNullable(true)).shouldBe("{\"value\":true}")
      }
      test("null") {
        mapper.writeValueAsString(MyClassNullable(null)).shouldBe("{\"value\":null}")
      }
    }
  }

  context("deserialize") {
    context("default") {
      test("false") {
        mapper.readValue<MyClass>("{ \"value\": false }").shouldBe(MyClass(false))
      }
      test("true") {
        mapper.readValue<MyClass>("{ \"value\": true }").shouldBe(MyClass(true))
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
    context("is") {
      test("false") {
        mapper.readValue<MyClassIs>("{ \"isValue\": false }").shouldBe(MyClassIs(false))
      }
      test("true") {
        mapper.readValue<MyClassIs>("{ \"isValue\": true }").shouldBe(MyClassIs(true))
      }
      test("null") {
        shouldThrow<MismatchedInputException> {
          mapper.readValue<MyClassIs>("{ \"isValue\": null }")
        }
      }
      test("missing") {
        shouldThrow<MismatchedInputException> {
          mapper.readValue<MyClassIs>("{}")
        }
      }
    }
    context("nullable") {
      test("false") {
        mapper.readValue<MyClassNullable>("{ \"value\": false }").shouldBe(MyClassNullable(false))
      }
      test("true") {
        mapper.readValue<MyClassNullable>("{ \"value\": true }").shouldBe(MyClassNullable(true))
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
        shouldThrow<InvalidFormatException> {
          mapper.readValue<MyClass>("{ \"value\": 0 }")
        }
      }
      test("float") {
        shouldThrow<MismatchedInputException> {
          mapper.readValue<MyClass>("{ \"value\": 1.0 }")
        }
      }
      test("string") {
        shouldThrow<MismatchedInputException> {
          mapper.readValue<MyClass>("{ \"value\": \"false\" }")
        }
      }
    }
  }
}) {
  private data class MyClass(
    val value: Boolean,
  )

  private data class MyClassIs(
    val isValue: Boolean,
  )

  private data class MyClassNullable(
    val value: Boolean?,
  )
}
