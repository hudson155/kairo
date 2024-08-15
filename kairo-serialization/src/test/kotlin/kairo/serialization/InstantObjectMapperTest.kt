package kairo.serialization

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.time.Instant

/**
 * This test is intended to test behaviour strictly related to instant serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to instants.
 */
internal class InstantObjectMapperTest : FunSpec({
  val mapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  context("serialize") {
    context("default") {
      test("recent") {
        mapper.writeValueAsString(MyClass(Instant.parse("2023-11-13T19:44:32.123456789Z")))
          .shouldBe("{\"value\":\"2023-11-13T19:44:32.123456789Z\"}")
      }
      test("old") {
        mapper.writeValueAsString(MyClass(Instant.parse("0005-01-01T00:00:00.000000000Z")))
          .shouldBe("{\"value\":\"0005-01-01T00:00:00Z\"}")
      }
    }
    context("nullable") {
      test("recent") {
        mapper.writeValueAsString(MyClassNullable(Instant.parse("2023-11-13T19:44:32.123456789Z")))
          .shouldBe("{\"value\":\"2023-11-13T19:44:32.123456789Z\"}")
      }
      test("old") {
        mapper.writeValueAsString(MyClassNullable(Instant.parse("0005-01-01T00:00:00.000000000Z")))
          .shouldBe("{\"value\":\"0005-01-01T00:00:00Z\"}")
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
        mapper.readValue<MyClass>("{ \"value\": \"2023-11-13T19:44:32.123456789Z\" }")
          .shouldBe(MyClass(Instant.parse("2023-11-13T19:44:32.123456789Z")))
      }
      test("old") {
        mapper.readValue<MyClass>("{ \"value\": \"0005-01-01T00:00:00Z\" }")
          .shouldBe(MyClass(Instant.parse("0005-01-01T00:00:00.000000000Z")))
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
        mapper.readValue<MyClassNullable>("{ \"value\": \"2023-11-13T19:44:32.123456789Z\" }")
          .shouldBe(MyClassNullable(Instant.parse("2023-11-13T19:44:32.123456789Z")))
      }
      test("old") {
        mapper.readValue<MyClassNullable>("{ \"value\": \"0005-01-01T00:00:00Z\" }")
          .shouldBe(MyClassNullable(Instant.parse("0005-01-01T00:00:00.000000000Z")))
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
        "{ \"value\":\"1-02-03T19:44:32.123456789Z\" }",
        "{ \"value\":\"2023-2-03T19:44:32.123456789Z\" }",
        "{ \"value\":\"2023-02-3T19:44:32.123456789Z\" }",
        "{ \"value\":\"2023-02-03T9:44:32.123456789Z\" }",
        "{ \"value\":\"2023-02-03T19:4:32.123456789Z\" }",
        "{ \"value\":\"2023-02-03T19:44:2.123456789Z\" }",
      ).forEachIndexed { i, string ->
        test("missing leading zero ($i)") {
          shouldThrow<JsonMappingException> {
            mapper.readValue<MyClassNullable>(string)
          }
        }
      }
      listOf(
        "{ \"value\":\"2023-02-00T19:44:32.123456789Z\" }",
        "{ \"value\":\"2023-02-29T19:44:32.123456789Z\" }",
        "{ \"value\":\"2023-02-03T25:44:32.123456789Z\" }",
        "{ \"value\":\"2023-02-03T19:60:32.123456789Z\" }",
        "{ \"value\":\"2023-02-03T19:44:60.123456789Z\" }",
        "{ \"value\":\"2023-02-03T19:44:32.1234567890Z\" }",
        "{ \"value\":\"2023-02-03T19:44:32.123456789\" }",
        "{ \"value\":\"2023-02-03T19:44:32.123456789-07:00[America/Edmonton]\" }",
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
          mapper.readValue<MyClass>("{ \"value\": 1699904672123 }")
        }
      }
    }
  }
}) {
  internal data class MyClass(
    val value: Instant,
  )

  internal data class MyClassNullable(
    val value: Instant?,
  )
}
