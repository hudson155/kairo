package kairo.config

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to [ConfigLoaderSource.EnvironmentVariable].
 */
internal class EnvironmentVariableConfigLoaderIntDefaultDeserializerTest : ConfigLoaderDeserializerTest() {
  /**
   * This test is specifically for non-nullable [Int] properties.
   */
  internal data class MyClass(
    val message: Int,
  )

  val stringWithDefault = """
    {
      "message": {
        "source": "EnvironmentVariable",
        "name": "MESSAGE",
        "default": "80"
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
    environmentVariable("8080")
    mapper.readValue<MyClass>(stringWithDefault).shouldBe(MyClass(8080))
  }

  @Test
  fun `non-null (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    environmentVariable("8080")
    mapper.readValue<MyClass>(stringWithDefault).shouldBe(MyClass(8080))
  }

  @Test
  fun `default (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    environmentVariable(null)
    mapper.readValue<MyClass>(stringWithDefault).shouldBe(MyClass(80))
  }

  @Test
  fun `default (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    environmentVariable(null)
    mapper.readValue<MyClass>(stringWithDefault).shouldBe(MyClass(80))
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
    every { environmentVariableSupplier.get("MESSAGE", any()) } answers {
      return@answers value ?: secondArg()
    }
  }
}