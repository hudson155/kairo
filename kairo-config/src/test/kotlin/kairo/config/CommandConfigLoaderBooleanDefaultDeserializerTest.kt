package kairo.config

import com.fasterxml.jackson.databind.JsonMappingException
import io.github.projectmapk.jackson.module.kogera.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to [ConfigLoaderSource.Command].
 */
internal class CommandConfigLoaderBooleanDefaultDeserializerTest : ConfigLoaderDeserializerTest() {
  /**
   * This test is specifically for non-nullable [Boolean] properties.
   */
  internal data class MyClass(
    val message: Boolean,
  )

  private val nonNullString: String = """
    {
      "message": {
        "source": "Command",
        "command": "echo \"true\""
      }
    }
  """.trimIndent()

  private val nullString: String = """
    {
      "message": {
        "source": "Command",
        "command": ";"
      }
    }
  """.trimIndent()

  private val incorrectTypeString: String = """
    {
      "message": {
        "source": "Command",
        "command": "echo \"Hello, World!\""
      }
    }
  """.trimIndent()

  @Test
  fun `non-null (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    shouldBeInsecure("Config loader source Command is considered insecure.") {
      mapper.readValue<MyClass>(nonNullString)
    }
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
    shouldBeInsecure("Config loader source Command is considered insecure.") {
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
