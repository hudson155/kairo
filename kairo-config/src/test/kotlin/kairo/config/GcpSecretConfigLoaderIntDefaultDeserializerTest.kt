package kairo.config

import com.fasterxml.jackson.databind.JsonMappingException
import io.github.projectmapk.jackson.module.kogera.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.every
import kairo.protectedString.ProtectedString
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to [ConfigLoaderSource.GcpSecret].
 */
@OptIn(ProtectedString.Access::class)
internal class GcpSecretConfigLoaderIntDefaultDeserializerTest : ConfigLoaderDeserializerTest() {
  /**
   * This test is specifically for non-nullable [Int] properties.
   */
  internal data class MyClass(
    val message: Int,
  )

  private val string: String = """
    {
      "message": {
        "source": "GcpSecret",
        "id": "projects/012345678900/secrets/example/versions/1"
      }
    }
  """.trimIndent()

  @Test
  fun `non-null (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    gcpSecret(ProtectedString("8080"))
    shouldBeInsecure("Config loader source GcpSecret is considered insecure.") {
      mapper.readValue<MyClass>(string)
    }
  }

  @Test
  fun `non-null (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    gcpSecret(ProtectedString("8080"))
    mapper.readValue<MyClass>(string)
  }

  @Test
  fun `null (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    gcpSecret(null)
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>(string)
    }
  }

  @Test
  fun `null (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    gcpSecret(null)
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>(string)
    }
  }

  @Test
  fun `incorrect type`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    gcpSecret(ProtectedString("Hello, World!"))
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>(string)
    }
  }

  private fun gcpSecret(value: ProtectedString?) {
    every { gcpSecretSupplier["projects/012345678900/secrets/example/versions/1"] } returns value
  }
}
