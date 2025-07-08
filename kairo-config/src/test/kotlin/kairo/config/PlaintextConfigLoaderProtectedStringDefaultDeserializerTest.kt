package kairo.config

import com.fasterxml.jackson.databind.JsonMappingException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kairo.protectedString.ProtectedString
import kairo.serialization.util.kairoRead
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to [ProtectedString] plaintext values.
 */
@OptIn(ProtectedString.Access::class)
internal class PlaintextConfigLoaderProtectedStringDefaultDeserializerTest : ConfigLoaderDeserializerTest() {
  /**
   * This test is specifically for non-nullable [ProtectedString] properties.
   */
  internal data class MyClass(
    val message: ProtectedString,
  )

  private val nonNullString: String = """
    {
      "message": "Hello, World!"
    }
  """.trimIndent()

  private val emptyString: String = """
    {
      "message": ""
    }
  """.trimIndent()

  private val nullString: String = """
    {
      "message": null
    }
  """.trimIndent()

  @Test
  fun `non-null (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    shouldThrow<JsonMappingException> {
      mapper.kairoRead<MyClass>(nonNullString)
    }
  }

  @Test
  fun `non-null (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    mapper.kairoRead<MyClass>(nonNullString).shouldBe(MyClass(ProtectedString("Hello, World!")))
  }

  @Test
  fun `empty (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    mapper.kairoRead<MyClass>(emptyString).shouldBe(MyClass(ProtectedString("")))
  }

  @Test
  fun `empty (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    mapper.kairoRead<MyClass>(emptyString).shouldBe(MyClass(ProtectedString("")))
  }

  @Test
  fun `null (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    shouldThrow<JsonMappingException> {
      mapper.kairoRead<MyClass>(nullString)
    }
  }

  @Test
  fun `null (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    shouldThrow<JsonMappingException> {
      mapper.kairoRead<MyClass>(nullString)
    }
  }
}
