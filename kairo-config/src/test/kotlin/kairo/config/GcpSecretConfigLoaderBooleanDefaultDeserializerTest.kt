package kairo.config

import com.fasterxml.jackson.databind.JsonMappingException
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.every
import kairo.protectedString.ProtectedString
import kairo.serialization.util.kairoRead
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to [ConfigLoaderSource.GcpSecret].
 */
@OptIn(ProtectedString.Access::class)
internal class GcpSecretConfigLoaderBooleanDefaultDeserializerTest : ConfigLoaderDeserializerTest() {
  /**
   * This test is specifically for non-nullable [Boolean] properties.
   */
  internal data class MyClass(
    val message: Boolean,
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
    gcpSecret(ProtectedString("true"))
    shouldBeInsecure("Config loader source GcpSecret is considered insecure.") {
      mapper.kairoRead<MyClass>(string)
    }
  }

  @Test
  fun `non-null (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    gcpSecret(ProtectedString("true"))
    mapper.kairoRead<MyClass>(string)
  }

  @Test
  fun `null (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    gcpSecret(null)
    shouldThrow<JsonMappingException> {
      mapper.kairoRead<MyClass>(string)
    }
  }

  @Test
  fun `null (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    gcpSecret(null)
    shouldThrow<JsonMappingException> {
      mapper.kairoRead<MyClass>(string)
    }
  }

  @Test
  fun `incorrect type`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    gcpSecret(ProtectedString("Hello, World!"))
    shouldThrow<JsonMappingException> {
      mapper.kairoRead<MyClass>(string)
    }
  }

  private fun gcpSecret(value: ProtectedString?) {
    every { gcpSecretSupplier["projects/012345678900/secrets/example/versions/1"] } returns value
  }
}
