package kairo.serialization

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

/**
 * This test is intended to test behaviour strictly related to local date serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to local dates.
 */
internal class LocalDateObjectMapperTest : FunSpec({
  val mapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  context("serialize") {
    context("default") {
      test("recent") {
        mapper.writeValueAsString(MyClass(LocalDate.parse("2023-11-13")))
          .shouldBe("{\"value\":\"2023-11-13\"}")
      }
      test("old") {
        mapper.writeValueAsString(MyClass(LocalDate.parse("0005-01-01")))
          .shouldBe("{\"value\":\"0005-01-01\"}")
      }
    }
    context("nullable") {
      test("recent") {
        mapper.writeValueAsString(MyClassNullable(LocalDate.parse("2023-11-13")))
          .shouldBe("{\"value\":\"2023-11-13\"}")
      }
      test("old") {
        mapper.writeValueAsString(MyClassNullable(LocalDate.parse("0005-01-01")))
          .shouldBe("{\"value\":\"0005-01-01\"}")
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
        mapper.readValue<MyClass>("{ \"value\": \"2023-11-13\" }")
          .shouldBe(MyClass(LocalDate.parse("2023-11-13")))
      }
      test("old") {
        mapper.readValue<MyClass>("{ \"value\": \"0005-01-01\" }")
          .shouldBe(MyClass(LocalDate.parse("0005-01-01")))
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
        mapper.readValue<MyClassNullable>("{ \"value\": \"2023-11-13\" }")
          .shouldBe(MyClassNullable(LocalDate.parse("2023-11-13")))
      }
      test("old") {
        mapper.readValue<MyClassNullable>("{ \"value\": \"0005-01-01\" }")
          .shouldBe(MyClassNullable(LocalDate.parse("0005-01-01")))
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
      listOf(
        "{ \"value\":\"1-02-03\" }",
        "{ \"value\":\"2023-2-03\" }",
        "{ \"value\":\"2023-02-3\" }",
      ).forEachIndexed { i, string ->
        test("missing leading zero ($i)") {
          shouldThrow<JsonMappingException> {
            mapper.readValue<MyClassNullable>(string)
          }
        }
      }
      listOf(
        "{ \"value\":\"2023-02-00\" }",
        "{ \"value\":\"2023-02-29\" }",
      ).forEachIndexed { i, string ->
        test("nonexistent date ($i)") {
          shouldThrow<JsonMappingException> {
            mapper.readValue<MyClassNullable>(string)
          }
        }
      }
    }
    context("wrong type") {
      test("number") {
        shouldThrow<MismatchedInputException> {
          mapper.readValue<MyClass>("{ \"value\": 20231113 }")
        }
      }
    }
  }
}) {
  internal data class MyClass(
    val value: LocalDate,
  )

  internal data class MyClassNullable(
    val value: LocalDate?,
  )
}
