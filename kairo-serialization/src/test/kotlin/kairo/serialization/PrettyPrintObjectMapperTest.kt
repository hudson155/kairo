package kairo.serialization

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

/**
 * This test is intended to test behaviour strictly related to pretty printing.
 * Therefore, some test cases are not included since they are not strictly related to pretty printing.
 */
internal class PrettyPrintObjectMapperTest : FunSpec({
  context("serialize") {
    test("prettyPrint = false (default)") {
      val mapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()
      val myClass = MyClass(
        string = "s v",
        map = mapOf("key1" to "val1", "key0" to "val0"),
      )
      val string = "{\"string\":\"s v\",\"map\":{\"key1\":\"val1\",\"key0\":\"val0\"},\"property\":42}"
      mapper.writeValueAsString(myClass)
        .shouldBe(string)
    }
    test("prettyPrint = true (default)") {
      val mapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json) { prettyPrint = true }.build()
      val myClass = MyClass(
        string = "string value",
        map = mapOf("second key" to "second value", "first key" to "first value"),
      )
      val string = """
        {
          "map": {
            "first key": "first value",
            "second key": "second value"
          },
          "property": 42,
          "string": "string value"
        }
      """.trimIndent()
      mapper.writeValueAsString(myClass).shouldBe(string)
    }
  }
}) {
  internal data class MyClass(
    val string: String,
    val map: Map<String, String>,
  ) {
    val property: Int = 42
  }
}
