package kairo.serialization

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

/**
 * This test is intended to test behaviour strictly related to long serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to longs.
 */
internal class LongObjectMapperTest : FunSpec({
  val mapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  context("serialize") {
    context("default") {
      test("positive") {
        mapper.writeValueAsString(MyClass(42L)).shouldBe("{\"value\":42}")
      }
      test("negative") {
        mapper.writeValueAsString(MyClass(-42L)).shouldBe("{\"value\":-42}")
      }
    }
    context("nullable") {
      test("positive") {
        mapper.writeValueAsString(MyClassNullable(42L)).shouldBe("{\"value\":42}")
      }
      test("negative") {
        mapper.writeValueAsString(MyClassNullable(-42L)).shouldBe("{\"value\":-42}")
      }
      test("null") {
        mapper.writeValueAsString(MyClassNullable(null)).shouldBe("{\"value\":null}")
      }
    }
  }

  context("deserialize") {
    context("default") {
      test("positive") {
        mapper.readValue<MyClass>("{ \"value\": 42 }").shouldBe(MyClass(42L))
      }
      test("negative") {
        mapper.readValue<MyClass>("{ \"value\": -42 }").shouldBe(MyClass(-42L))
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
        mapper.readValue<MyClassNullable>("{ \"value\": 42 }").shouldBe(MyClassNullable(42L))
      }
      test("negative") {
        mapper.readValue<MyClassNullable>("{ \"value\": -42 }").shouldBe(MyClassNullable(-42L))
      }
      test("null") {
        mapper.readValue<MyClassNullable>("{ \"value\": null }").shouldBe(MyClassNullable(null))
      }
      test("missing") {
        mapper.readValue<MyClassNullable>("{}").shouldBe(MyClassNullable(null))
      }
    }
    context("wrong type") {
      test("float") {
        shouldThrow<InvalidFormatException> {
          mapper.readValue<MyClass>("{ \"value\": 1.23 }")
        }
      }
      test("string") {
        shouldThrow<MismatchedInputException> {
          mapper.readValue<MyClass>("{ \"value\": \"42\" }")
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
    val value: Long,
  )

  private data class MyClassNullable(
    val value: Long?,
  )
}
