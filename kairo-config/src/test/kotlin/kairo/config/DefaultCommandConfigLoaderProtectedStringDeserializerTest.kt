package kairo.config

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kairo.protectedString.ProtectedString
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to [ConfigLoaderProtectedStringSource.Command].
 */
internal class DefaultCommandConfigLoaderProtectedStringDeserializerTest : ConfigLoaderDeserializerTest() {
  /**
   * This test is specifically for non-nullable properties.
   */
  internal data class MyClass(
    val message: ProtectedString,
  )

  val nonNullString = """
    {
      "message": {
        "source": "Command",
        "command": "echo \"Hello, World!\""
      }
    }
  """.trimIndent()

  val nullString = """
    {
      "message": {
        "source": "Command",
        "command": ";"
      }
    }
  """.trimIndent()

  @Test
  fun `non-null (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    shouldBeInsecure("Command source is considered insecure.") {
      mapper.readValue<MyClass>(nonNullString)
    }
  }

  @Test
  fun `non-null (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    mapper.readValue<MyClass>(nonNullString).shouldBe(MyClass(ProtectedString("Hello, World!")))
  }

  @Test
  fun `null (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    shouldBeInsecure("Command source is considered insecure.") {
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
}
