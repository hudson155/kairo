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
 * This test is intended to test behaviour strictly related to [ConfigLoaderSource.GcpSecret].
 */
@OptIn(ProtectedString.Access::class)
internal class GcpSecretConfigLoaderProtectedStringDefaultDeserializerTest : ConfigLoaderDeserializerTest() {
  /**
   * This test is specifically for non-nullable [ProtectedString] properties.
   */
  internal data class MyClass(
    val message: ProtectedString,
  )

  val string = """
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
    mapper.readValue<MyClass>(string).shouldBe(MyClass(ProtectedString("Hello, World!")))
  }

  @Test
  fun `non-null (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    gcpSecret(ProtectedString("Hello, World!"))
    mapper.readValue<MyClass>(string).shouldBe(MyClass(ProtectedString("Hello, World!")))
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

  private fun gcpSecret(value: ProtectedString?) {
    every { gcpSecretSupplier.get("projects/012345678900/secrets/example/versions/1") } returns value
  }
}