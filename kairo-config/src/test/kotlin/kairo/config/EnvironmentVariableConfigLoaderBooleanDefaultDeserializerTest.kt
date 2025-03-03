package kairo.config

import com.fasterxml.jackson.databind.JsonMappingException
import io.github.projectmapk.jackson.module.kogera.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to [ConfigLoaderSource.EnvironmentVariable].
 */
internal class EnvironmentVariableConfigLoaderBooleanDefaultDeserializerTest : ConfigLoaderDeserializerTest() {
  /**
   * This test is specifically for non-nullable [Boolean] properties.
   */
  internal data class MyClass(
    val message: Boolean,
  )

  private val stringWithDefault: String = """
    {
      "message": {
        "source": "EnvironmentVariable",
        "name": "MESSAGE",
        "default": "false"
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
    environmentVariable("true")
    mapper.readValue<MyClass>(stringWithDefault).shouldBe(MyClass(true))
  }

  @Test
  fun `non-null (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    environmentVariable("true")
    mapper.readValue<MyClass>(stringWithDefault).shouldBe(MyClass(true))
  }

  @Test
  fun `default (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    environmentVariable(null)
    mapper.readValue<MyClass>(stringWithDefault).shouldBe(MyClass(false))
  }

  @Test
  fun `default (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    environmentVariable(null)
    mapper.readValue<MyClass>(stringWithDefault).shouldBe(MyClass(false))
  }

  @Test
  fun `null (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    environmentVariable(null)
    shouldThrow<JsonMappingException> {
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

  @Test
  fun `incorrect type`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    environmentVariable("Hello, World!")
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>(stringWithDefault)
    }
  }

  private fun environmentVariable(value: String?) {
    every { environmentVariableSupplier["MESSAGE", any()] } answers {
      return@answers value ?: secondArg()
    }
  }
}
