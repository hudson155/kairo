package kairo.config

import com.fasterxml.jackson.databind.JsonMappingException
import io.github.projectmapk.jackson.module.kogera.readValue
import io.kotest.assertions.throwables.shouldThrow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class UnsupportedSourceConfigLoaderStringDeserializerTest : ConfigLoaderDeserializerTest() {
  internal data class MyClass(
    val message: String,
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
      mapper.readValue<MyClass>(string)
    }
  }

  @Test
  fun `(allowInsecureConfigSources = true)`(): Unit = runTest {
    allowInsecureConfigSources(true)
    val mapper = createMapper()
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>(string)
    }
  }
}
