package kairo.config

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to [Boolean] plaintext values.
 */
internal class PlaintextConfigLoaderBooleanNullableDeserializerTest : ConfigLoaderDeserializerTest() {
  /**
   * This test is specifically for nullable [Boolean] properties.
   */
  internal data class MyClass(
    val message: Boolean?,
  )

  private val nonNullString: String = """
    {
      "message": true
    }
  """.trimIndent()

  private val nullString = """
    {
      "message": null
    }
  """.trimIndent()

  private val incorrectTypeString = """
    {
      "message": "Hello, World!"
    }
  """.trimIndent()

  @Test
  fun `non-null (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    mapper.readValue<MyClass>(nonNullString).shouldBe(MyClass(true))
  }

  @Test
  fun `non-null (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    mapper.readValue<MyClass>(nonNullString).shouldBe(MyClass(true))
  }

  @Test
  fun `null (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    mapper.readValue<MyClass>(nullString).shouldBe(MyClass(null))
  }

  @Test
  fun `null (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    mapper.readValue<MyClass>(nullString).shouldBe(MyClass(null))
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
