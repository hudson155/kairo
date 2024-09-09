package kairo.config

import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import kairo.protectedString.ProtectedString
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to [ProtectedString] plaintext values.
 */
internal class NullablePlainStringConfigLoaderProtectedStringDeserializerTest : ConfigLoaderDeserializerTest() {
  /**
   * This test is specifically for nullable properties.
   */
  internal data class MyClass(
    val message: ProtectedString?,
  )

  private val nonNullString: String = """
    {
      "message": "Hello, World!"
    }
  """.trimIndent()

  private val nullString = """
    {
      "message": null
    }
  """.trimIndent()

  @Test
  fun `non-null (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    shouldBeInsecure("Protected strings cannot appear directly in configs.") {
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
    mapper.readValue<MyClass>(nullString).shouldBe(MyClass(null))
  }

  @Test
  fun `null (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    mapper.readValue<MyClass>(nullString).shouldBe(MyClass(null))
  }
}
