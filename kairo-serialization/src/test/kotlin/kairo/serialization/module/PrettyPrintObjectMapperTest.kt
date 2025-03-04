package kairo.serialization.module

import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kairo.serialization.property.prettyPrint
import kairo.serialization.util.kairoWrite
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to pretty printing.
 * Therefore, some test cases are not included since they are not strictly related to pretty printing.
 */
internal class PrettyPrintObjectMapperTest {
  internal data class MyClass(
    val string: String,
    val map: Map<String, String>,
  ) {
    val property: Int = 42
  }

  @Test
  fun `serialize, prettyPrint = false (default)`(): Unit = runTest {
    val mapper = jsonMapper().build()
    val myClass = MyClass(
      string = "s v",
      map = mapOf("key1" to "val1", "key0" to "val0"),
    )
    val string = "{\"string\":\"s v\",\"map\":{\"key1\":\"val1\",\"key0\":\"val0\"},\"property\":42}"
    mapper.kairoWrite(myClass)
      .shouldBe(string)
  }

  @Test
  fun `serialize, prettyPrint = true`(): Unit = runTest {
    val mapper = jsonMapper {
      prettyPrint = true
    }.build()
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
    mapper.kairoWrite(myClass).shouldBe(string)
  }
}
