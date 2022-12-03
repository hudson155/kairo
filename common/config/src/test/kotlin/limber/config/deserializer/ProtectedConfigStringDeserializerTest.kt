package limber.config.deserializer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import limber.config.addConfigDeserializers
import limber.serialization.ObjectMapperFactory
import limber.type.ProtectedString
import org.junit.jupiter.api.Test

internal class ProtectedConfigStringDeserializerTest {
  private val objectMapper: ObjectMapper = ObjectMapperFactory.builder(ObjectMapperFactory.Format.Json) {
    module.addConfigDeserializers()
  }.build()

  data class Config(val someValue: ProtectedString?)

  init {
    CommandSource.withOverride({ " " }) {}
    EnvironmentVariableSource.withOverride({ " " }) {}
    GcpSecretSource.withOverride({ " " }) {}
  }

  @Test
  fun `plaintext - value is set`() {
    val map = mapOf("someValue" to "the value")
    objectMapper.convertValue<Config>(map).someValue.shouldBe(ProtectedString("the value"))
  }

  @Test
  fun `plaintext - value is null`() {
    val map = mapOf("someValue" to null)
    objectMapper.convertValue<ConfigStringDeserializerTest.Config>(map).someValue.shouldBeNull()
  }

  @Test
  fun `plaintext - value not set`() {
    val map = emptyMap<String, String>()
    objectMapper.convertValue<ConfigStringDeserializerTest.Config>(map).someValue.shouldBeNull()
  }

  @Test
  fun `environment variable - value is set`() {
    EnvironmentVariableSource.withOverride({ "val from env" }) {
      val map = mapOf(
        "someValue" to mapOf(
          "type" to "EnvironmentVariable",
          "name" to "TEST_ENV_VAR",
        ),
      )
      objectMapper.convertValue<Config>(map).someValue.shouldBe(ProtectedString("val from env"))
    }
  }

  @Test
  fun `environment variable - value not set`() {
    EnvironmentVariableSource.withOverride({ null }) {
      val map = mapOf(
        "someValue" to mapOf(
          "type" to "EnvironmentVariable",
          "name" to "TEST_ENV_VAR",
        ),
      )
      objectMapper.convertValue<Config>(map).someValue.shouldBeNull()
    }
  }

  @Test
  fun `gcp secret - value is set`() {
    GcpSecretSource.withOverride({ "val from gcp" }) {
      val map = mapOf(
        "someValue" to mapOf(
          "type" to "GcpSecret",
          "id" to "projects/123/secrets/my-secret",
        ),
      )
      objectMapper.convertValue<Config>(map).someValue.shouldBe(ProtectedString("val from gcp"))
    }
  }

  @Test
  fun `gcp secret - value not set`() {
    GcpSecretSource.withOverride({ error("message") }) {
      val map = mapOf(
        "someValue" to mapOf(
          "type" to "GcpSecret",
          "id" to "projects/123/secrets/my-secret",
        ),
      )
      shouldThrow<IllegalArgumentException> { objectMapper.convertValue<Config>(map) }
    }
  }

  @Test
  fun `command - returns value`() {
    val map = mapOf(
      "someValue" to mapOf(
        "type" to "Command",
        "command" to "echo \"val from cmd\"",
      ),
    )
    objectMapper.convertValue<Config>(map).someValue.shouldBe(ProtectedString("val from cmd"))
  }

  @Test
  fun `command - returns nothing`() {
    val map = mapOf(
      "someValue" to mapOf(
        "type" to "Command",
        "command" to ";",
      ),
    )
    shouldThrow<IllegalArgumentException> { objectMapper.convertValue<Config>(map) }
  }
}
