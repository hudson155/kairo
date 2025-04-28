package kairo.config

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to [String] plaintext values.
 */
internal class PlaintextConfigLoaderStringDefaultDeserializerTest : ConfigLoaderDeserializerTest() {
  /**
   * This test is specifically for non-nullable [String] properties.
   */
  internal data class MyClass(
    val message: String,
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

  private val nullString = """
    {
      "message": null
    }
  """.trimIndent()

  private val incorrectTypeString = """
    {
      "message": 8080
    }
  """.trimIndent()

  @Test
  fun `non-null (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    mapper.readValue<MyClass>(nonNullString).shouldBe(MyClass("Hello, World!"))
  }

  @Test
  fun `non-null (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    mapper.readValue<MyClass>(nonNullString).shouldBe(MyClass("Hello, World!"))
  }

  @Test
  fun `null (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>(nullString)
    }
  }

  @Test
  fun `empty (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    mapper.readValue<MyClass>(emptyString).shouldBe(MyClass(""))
  }

  @Test
  fun `empty (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    mapper.readValue<MyClass>(emptyString).shouldBe(MyClass(""))
  }

  @Test
  fun `null (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>(nullString)
    }
  }

  @Test
  fun `incorrect type`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>(incorrectTypeString)
    }
  }
}
