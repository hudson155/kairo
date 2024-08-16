package kairo.serialization

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.util.UUID

/**
 * This test is intended to test behaviour strictly related to UUID serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to UUIDs.
 */
internal class UuidObjectMapperTest : FunSpec({
  val mapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  context("serialize") {
    context("default") {
      test("default") {
        mapper.writeValueAsString(MyClass(UUID.fromString("3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8")))
          .shouldBe("{\"value\":\"3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8\"}")
      }
    }
    context("nullable") {
      test("default") {
        mapper.writeValueAsString(MyClassNullable(UUID.fromString("3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8")))
          .shouldBe("{\"value\":\"3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8\"}")
      }
      test("null") {
        mapper.writeValueAsString(MyClassNullable(null))
          .shouldBe("{\"value\":null}")
      }
    }
  }

  context("deserialize") {
    context("default") {
      test("recent") {
        mapper.readValue<MyClass>("{ \"value\": \"3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8\" }")
          .shouldBe(MyClass(UUID.fromString("3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8")))
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
      test("recent") {
        mapper.readValue<MyClassNullable>("{ \"value\": \"3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8\" }")
          .shouldBe(MyClassNullable(UUID.fromString("3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8")))
      }
      test("null") {
        mapper.readValue<MyClassNullable>("{ \"value\": null }")
          .shouldBe(MyClassNullable(null))
      }
      test("missing") {
        mapper.readValue<MyClassNullable>("{}")
          .shouldBe(MyClassNullable(null))
      }
    }
    context("wrong format") {
      test("too short") {
        shouldThrow<JsonMappingException> {
          mapper.readValue<MyClassNullable>("{ \"value\": \"3ec0a853-dae3-4ee1-abe2-0b9c7dee45f\" }")
        }
      }
      test("too long") {
        shouldThrow<JsonMappingException> {
          mapper.readValue<MyClassNullable>("{ \"value\": \"3ec0a853-dae3-4ee1-abe2-0b9c7dee45f88\" }")
        }
      }
      test("missing dashes") {
        shouldThrow<JsonMappingException> {
          mapper.readValue<MyClassNullable>("{ \"value\": \"3ec0a853dae34ee1abe20b9c7dee45f8\" }")
        }
      }
    }
    context("wrong type") {
      test("number") {
        shouldThrow<JsonParseException> {
          mapper.readValue<MyClass>("{ \"value\": 00001111222233334444555566667777 }")
        }
      }
    }
  }
}) {
  internal data class MyClass(
    val value: UUID,
  )

  internal data class MyClassNullable(
    val value: UUID?,
  )
}
