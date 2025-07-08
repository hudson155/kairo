package kairo.config

import com.fasterxml.jackson.databind.JsonMappingException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kairo.serialization.util.kairoRead
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to [String] plaintext values.
 */
internal class PlaintextConfigLoaderStringNullableDeserializerTest : ConfigLoaderDeserializerTest() {
  /**
   * This test is specifically for nullable [String] properties.
   */
  internal data class MyClass(
    val message: String?,
  )

  private val nonNullString: String = """
    {
      "message": "Hello, World!"
    }
  """.trimIndent()

  private val emptyString: String = """
    {
      "message": ""
    }
  """.trimIndent()

  private val nullString: String = """
    {
      "message": null
    }
  """.trimIndent()

  private val incorrectTypeString: String = """
    {
      "message": 8080
    }
  """.trimIndent()

  @Test
  fun `non-null (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    mapper.kairoRead<MyClass>(nonNullString).shouldBe(MyClass("Hello, World!"))
  }

  @Test
  fun `non-null (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    mapper.kairoRead<MyClass>(nonNullString).shouldBe(MyClass("Hello, World!"))
  }

  @Test
  fun `empty (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    mapper.kairoRead<MyClass>(emptyString).shouldBe(MyClass(""))
  }

  @Test
  fun `empty (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    mapper.kairoRead<MyClass>(emptyString).shouldBe(MyClass(""))
  }

  @Test
  fun `null (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    mapper.kairoRead<MyClass>(nullString).shouldBe(MyClass(null))
  }

  @Test
  fun `null (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    mapper.kairoRead<MyClass>(nullString).shouldBe(MyClass(null))
  }

  @Test
  fun `incorrect type`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    shouldThrow<JsonMappingException> {
      mapper.kairoRead<MyClass>(incorrectTypeString)
    }
  }
}
