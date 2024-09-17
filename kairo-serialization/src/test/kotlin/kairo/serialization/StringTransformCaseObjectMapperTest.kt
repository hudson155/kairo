package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to string "transform case" during deserialization.
 * Therefore, some test cases are not included since they are not strictly related to string "transform case".
 */
internal class StringTransformCaseObjectMapperTest {
  internal data class MyClass(
    val value: String,
  )

  data class MyClassLowercase(
    @TransformCase(TransformCase.Type.Lowercase)
    val value: String,
  )

  data class MyClassUppercase(
    @TransformCase(TransformCase.Type.Uppercase)
    val value: String,
  )

  private val mapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  private val string: String = "{ \"value\": \"Asdf\" }"

  @Test
  fun `deserialize, none`(): Unit = runTest {
    mapper.readValue<MyClass>(string).shouldBe(MyClass("Asdf"))
  }

  @Test
  fun `deserialize, lowercase, override`(): Unit = runTest {
    mapper.readValue<MyClassLowercase>(string).shouldBe(MyClassLowercase("asdf"))
  }

  @Test
  fun `deserialize, uppercase, override`(): Unit = runTest {
    mapper.readValue<MyClassUppercase>(string).shouldBe(MyClassUppercase("ASDF"))
  }
}
