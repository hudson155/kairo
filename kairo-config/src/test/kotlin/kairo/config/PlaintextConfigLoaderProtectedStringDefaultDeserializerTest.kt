package kairo.config

import com.fasterxml.jackson.databind.JsonMappingException
import io.github.projectmapk.jackson.module.kogera.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kairo.protectedString.ProtectedString
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

  private val nullString = """
    {
      "message": null
    }
  """.trimIndent()

  @Test
  fun `non-null (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    shouldThrow<JsonMappingException> {
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
  fun `empty (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    mapper.readValue<MyClass>(emptyString).shouldBe(MyClass(ProtectedString("")))
  }

  @Test
  fun `empty (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    mapper.readValue<MyClass>(emptyString).shouldBe(MyClass(ProtectedString("")))
  }

  @Test
  fun `null (allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>(nullString)
    }
  }

  @Test
  fun `null (allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>(nullString)
    }
  }
}
