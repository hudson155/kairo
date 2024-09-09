package kairo.config

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import kairo.protectedString.ProtectedString
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to [ConfigLoaderProtectedStringSource.EnvironmentVariable].
 */
internal class DefaultEnvironmentVariableConfigLoaderProtectedStringDeserializerTest : ConfigLoaderDeserializerTest() {
  /**
   * This test is specifically for non-nullable properties.
   */
  internal data class MyClass(
    val message: ProtectedString,
  )

  val stringWithDefault = """
    {
      "message": {
        "source": "EnvironmentVariable",
        "name": "MESSAGE",
        "default": "Default value."
      }
    }
  """.trimIndent()

  val stringWithoutDefault = """
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
    shouldBeInsecure("Environment variable source is considered insecure.") {
      mapper.readValue<MyClass>(stringWithDefault)
    }
  }

  @Test
  fun `non-null (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    environmentVariable("Hello, World!")
    mapper.readValue<MyClass>(stringWithDefault).shouldBe(MyClass(ProtectedString("Hello, World!")))
  }

  @Test
  fun `default (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    environmentVariable(null)
    shouldBeInsecure("Environment variable source is considered insecure.") {
      mapper.readValue<MyClass>(stringWithDefault)
    }
  }

  @Test
  fun `default (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    environmentVariable(null)
    mapper.readValue<MyClass>(stringWithDefault).shouldBe(MyClass(ProtectedString("Default value.")))
  }

  @Test
  fun `null (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    environmentVariable(null)
    shouldBeInsecure("Environment variable source is considered insecure.") {
      mapper.readValue<MyClass>(stringWithoutDefault)
    }
  }

  @Test
  fun `null (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    environmentVariable(null)
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>(stringWithoutDefault)
    }
  }

  private fun environmentVariable(value: String?) {
    every { environmentVariableSupplier.get("MESSAGE", any()) } answers {
      return@answers value ?: secondArg()
    }
  }
}
