package kairo.serialization

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.util.Optional

/**
 * This test is intended to test behaviour strictly related to optional serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to optionals.
 *
 * The behaviour here might seem a bit odd at first,
 * since null is serialized to missing and Optional.empty() is serialized to null.
 * However, this approach is in fact necessary to provide consistency between nullable and non-nullable Optionals.
 */
internal class OptionalObjectMapperTest : FunSpec({
  val mapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  context("serialize") {
    context("default") {
      test("present") {
        mapper.writeValueAsString(MyClass(Optional.of(42))).shouldBe("{\"value\":42}")
      }
      test("empty") {
        mapper.writeValueAsString(MyClass(Optional.empty())).shouldBe("{\"value\":null}")
      }
    }
    context("nullable") {
      test("present") {
        mapper.writeValueAsString(MyClassNullable(Optional.of(42))).shouldBe("{\"value\":42}")
      }
      test("empty") {
        mapper.writeValueAsString(MyClassNullable(Optional.empty())).shouldBe("{\"value\":null}")
      }
      test("null") {
        mapper.writeValueAsString(MyClassNullable(null)).shouldBe("{}")
      }
    }
  }

  context("deserialize") {
    context("default") {
      test("present") {
        mapper.readValue<MyClass>("{ \"value\": 42 }").shouldBe(MyClass(Optional.of(42)))
      }
      test("empty") {
        mapper.readValue<MyClass>("{ \"value\": null }").shouldBe(MyClass(Optional.empty()))
      }
      test("missing") {
        shouldThrow<MismatchedInputException> {
          mapper.readValue<MyClass>("{}")
        }
      }
    }
    context("nullable") {
      test("present") {
        mapper.readValue<MyClassNullable>("{ \"value\": 42 }").shouldBe(MyClassNullable(Optional.of(42)))
      }
      test("empty") {
        mapper.readValue<MyClassNullable>("{ \"value\": null }").shouldBe(MyClassNullable(Optional.empty()))
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
  @JsonInclude(JsonInclude.Include.NON_NULL)
  internal data class MyClass(
    val value: Optional<Int>,
  )

  @JsonInclude(JsonInclude.Include.NON_NULL)
  internal data class MyClassNullable(
    val value: Optional<Int>?,
  )
}
