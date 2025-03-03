package kairo.config

import com.fasterxml.jackson.databind.JsonMappingException
import io.github.projectmapk.jackson.module.kogera.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to [Int] plaintext values.
 */
internal class PlaintextConfigLoaderIntDefaultDeserializerTest : ConfigLoaderDeserializerTest() {
  /**
   * This test is specifically for non-nullable [Int] properties.
   */
  internal data class MyClass(
    val message: Int,
  )

  private val nonNullString: String = """
    {
      "message": 8080
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
    mapper.readValue<MyClass>(nonNullString).shouldBe(MyClass(8080))
  }

  @Test
  fun `non-null (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    mapper.readValue<MyClass>(nonNullString).shouldBe(MyClass(8080))
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
