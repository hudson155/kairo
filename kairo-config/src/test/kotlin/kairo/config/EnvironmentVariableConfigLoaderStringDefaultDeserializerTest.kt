package kairo.config

import com.fasterxml.jackson.databind.JsonMappingException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import kairo.serialization.util.kairoRead
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to [ConfigLoaderSource.EnvironmentVariable].
 */
internal class EnvironmentVariableConfigLoaderStringDefaultDeserializerTest : ConfigLoaderDeserializerTest() {
  /**
   * This test is specifically for non-nullable [String] properties.
   */
  internal data class MyClass(
    val message: String,
  )

  private val stringWithDefault: String = """
    {
      "message": {
        "source": "EnvironmentVariable",
        "name": "MESSAGE",
        "default": "Default value."
      }
    }
  """.trimIndent()

  private val stringWithoutDefault: String = """
    {
      "message": {
        "source": "EnvironmentVariable",
        "name": "MESSAGE"
      }
    }
  """.trimIndent()

  @Test
  fun `non-null (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    environmentVariable("Hello, World!")
    mapper.kairoRead<MyClass>(stringWithDefault).shouldBe(MyClass("Hello, World!"))
  }

  @Test
  fun `non-null (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    environmentVariable("Hello, World!")
    mapper.kairoRead<MyClass>(stringWithDefault).shouldBe(MyClass("Hello, World!"))
  }

  @Test
  fun `default (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    environmentVariable(null)
    mapper.kairoRead<MyClass>(stringWithDefault).shouldBe(MyClass("Default value."))
  }

  @Test
  fun `default (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    environmentVariable(null)
    mapper.kairoRead<MyClass>(stringWithDefault).shouldBe(MyClass("Default value."))
  }

  @Test
  fun `null (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    environmentVariable(null)
    shouldThrow<JsonMappingException> {
      mapper.kairoRead<MyClass>(stringWithoutDefault)
    }
  }

  @Test
  fun `null (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    environmentVariable(null)
    shouldThrow<JsonMappingException> {
      mapper.kairoRead<MyClass>(stringWithoutDefault)
    }
  }

  private fun environmentVariable(value: String?) {
    every { environmentVariableSupplier["MESSAGE", any()] } answers {
      value ?: secondArg()
    }
  }
}
