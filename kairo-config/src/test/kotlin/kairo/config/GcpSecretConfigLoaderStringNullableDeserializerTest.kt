package kairo.config

import io.kotest.matchers.shouldBe
import io.mockk.every
import kairo.protectedString.ProtectedString
import kairo.serialization.util.kairoRead
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to [ConfigLoaderSource.GcpSecret].
 */
@OptIn(ProtectedString.Access::class)
internal class GcpSecretConfigLoaderStringNullableDeserializerTest : ConfigLoaderDeserializerTest() {
  /**
   * This test is specifically for nullable [String] properties.
   */
  internal data class MyClass(
    val message: String?,
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
    gcpSecret(ProtectedString("Hello, World!"))
    shouldBeInsecure("Config loader source GcpSecret is considered insecure.") {
      mapper.kairoRead<MyClass>(string)
    }
  }

  @Test
  fun `non-null (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    gcpSecret(ProtectedString("Hello, World!"))
    mapper.kairoRead<MyClass>(string)
  }

  @Test
  fun `null (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    gcpSecret(null)
    shouldBeInsecure("Config loader source GcpSecret is considered insecure.") {
      mapper.kairoRead<MyClass>(string).shouldBe(MyClass("Hello, World!"))
    }
  }

  @Test
  fun `null (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    gcpSecret(null)
    mapper.kairoRead<MyClass>(string).shouldBe(MyClass(null))
  }

  private fun gcpSecret(value: ProtectedString?) {
    every { gcpSecretSupplier["projects/012345678900/secrets/example/versions/1"] } returns value
  }
}
