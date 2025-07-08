package kairo.config

import com.fasterxml.jackson.databind.JsonMappingException
import io.kotest.assertions.throwables.shouldThrow
import kairo.protectedString.ProtectedString
import kairo.serialization.util.kairoRead
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class UnsupportedSourceConfigLoaderProtectedStringDeserializerTest : ConfigLoaderDeserializerTest() {
  internal data class MyClass(
    val message: ProtectedString,
  )

  private val string: String = """
    {
      "message": {
        "source": "Other"
      }
    }
  """.trimIndent()

  @Test
  fun `(allowInsecureConfigSources = false)`(): Unit = runTest {
    allowInsecureConfigSources(false)
    val mapper = createMapper()
    shouldThrow<JsonMappingException> {
      mapper.kairoRead<MyClass>(string)
    }
  }

  @Test
  fun `(allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    shouldThrow<JsonMappingException> {
      mapper.kairoRead<MyClass>(string)
    }
  }
}
