package kairo.serialization

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

/**
 * This test is intended to test behaviour strictly related to float serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to floats.
 */
internal class FloatObjectMapperTest : FunSpec({
  val mapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  context("serialize") {
    context("default") {
      test("positive") {
        mapper.writeValueAsString(MyClass(1.23F)).shouldBe("{\"value\":1.23}")
      }
      test("negative") {
        mapper.writeValueAsString(MyClass(-1.23F)).shouldBe("{\"value\":-1.23}")
      }
    }
    context("nullable") {
      test("positive") {
        mapper.writeValueAsString(MyClassNullable(1.23F)).shouldBe("{\"value\":1.23}")
      }
      test("negative") {
        mapper.writeValueAsString(MyClassNullable(-1.23F)).shouldBe("{\"value\":-1.23}")
      }
      test("null") {
        mapper.writeValueAsString(MyClassNullable(null)).shouldBe("{\"value\":null}")
      }
    }
  }

  context("deserialize") {
    context("default") {
      test("positive") {
        mapper.readValue<MyClass>("{ \"value\": 1.23 }").shouldBe(MyClass(1.23F))
      }
      test("negative") {
        mapper.readValue<MyClass>("{ \"value\": -1.23 }").shouldBe(MyClass(-1.23F))
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
      test("positive") {
        mapper.readValue<MyClassNullable>("{ \"value\": 1.23 }").shouldBe(MyClassNullable(1.23F))
      }
      test("negative") {
        mapper.readValue<MyClassNullable>("{ \"value\": -1.23 }").shouldBe(MyClassNullable(-1.23F))
      }
      test("null") {
        mapper.readValue<MyClassNullable>("{ \"value\": null }").shouldBe(MyClassNullable(null))
      }
      test("missing") {
        mapper.readValue<MyClassNullable>("{}").shouldBe(MyClassNullable(null))
      }
    }
    context("wrong type") {
      test("int") {
        mapper.readValue<MyClass>("{ \"value\": 42 }").shouldBe(MyClass(42.0F))
      }
      test("string") {
        shouldThrow<MismatchedInputException> {
          mapper.readValue<MyClass>("{ \"value\": \"1.23\" }")
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
  internal data class MyClass(
    val value: Float,
  )

  internal data class MyClassNullable(
    val value: Float?,
  )
}
