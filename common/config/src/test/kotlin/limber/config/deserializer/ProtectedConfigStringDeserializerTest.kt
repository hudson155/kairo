package limber.config.deserializer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.module.kotlin.convertValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import limber.serialization.ObjectMapperFactory
import limber.type.ProtectedString
import org.junit.jupiter.api.Test

internal class ProtectedConfigStringDeserializerTest {
  private val objectMapper: ObjectMapper = ObjectMapperFactory.builder(ObjectMapperFactory.Format.JSON).build()

  data class Config(
    @JsonDeserialize(using = ProtectedConfigStringDeserializer::class)
    val someValue: ProtectedString?,
  )

  init {
    CommandSource.withOverride({ " " }) {}
    EnvironmentVariableSource.withOverride({ " " }) {}
    GcpSecretSource.withOverride({ " " }) {}
  }

  @Test
  fun `used incorrectly (with raw string)`() {
    val map = mapOf("someValue" to "the value")
    shouldThrow<IllegalArgumentException> { objectMapper.convertValue<Config>(map) }
  }

  @Test
  fun `plaintext - value is set`() {
    val map = mapOf(
      "someValue" to mapOf(
        "type" to "Plaintext",
        "value" to "the value",
      ),
    )
    objectMapper.convertValue<Config>(map).someValue.shouldBe(ProtectedString("the value"))
  }

  @Test
  fun `plaintext - value not set`() {
    val map = mapOf(
      "someValue" to mapOf(
        "type" to "Plaintext",
      ),
    )
    objectMapper.convertValue<Config>(map).someValue.shouldBeNull()
  }

  @Test
  fun `environment variable - value is set`() {
    EnvironmentVariableSource.withOverride({ "val from env" }) {
      val map = mapOf(
        "someValue" to mapOf(
          "type" to "EnvironmentVariable",
          "value" to "TEST_ENV_VAR",
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
          "value" to "TEST_ENV_VAR",
        ),
      )
      objectMapper.convertValue<Config>(map).someValue.shouldBeNull()
    }
  }

  @Test
  fun `gcp secret - value is set`() {
    EnvironmentVariableSource.withOverride({ "id" }) {
      GcpSecretSource.withOverride({ "val from gcp" }) {
        val map = mapOf(
          "someValue" to mapOf(
            "type" to "GcpSecret",
            "value" to "TEST_ENV_VAR",
          ),
        )
        objectMapper.convertValue<Config>(map).someValue.shouldBe(ProtectedString("val from gcp"))
      }
    }
  }

  @Test
  fun `gcp secret - secret is not set`() {
    EnvironmentVariableSource.withOverride({ "id" }) {
      GcpSecretSource.withOverride({ null }) {
        val map = mapOf(
          "someValue" to mapOf(
            "type" to "GcpSecret",
            "value" to "TEST_ENV_VAR",
          ),
        )
        objectMapper.convertValue<Config>(map).someValue.shouldBeNull()
      }
    }
  }

  @Test
  fun `gcp secret - environment variable is not set`() {
    EnvironmentVariableSource.withOverride({ null }) {
      GcpSecretSource.withOverride({ "val from gcp" }) {
        val map = mapOf(
          "someValue" to mapOf(
            "type" to "GcpSecret",
            "value" to "TEST_ENV_VAR",
          ),
        )
        shouldThrow<IllegalArgumentException> { objectMapper.convertValue<Config>(map) }
      }
    }
  }

  @Test
  fun `command - returns value`() {
    val map = mapOf(
      "someValue" to mapOf(
        "type" to "Command",
        "value" to "echo \"val from cmd\"",
      ),
    )
    objectMapper.convertValue<Config>(map).someValue.shouldBe(ProtectedString("val from cmd"))
  }

  @Test
  fun `command - returns nothing`() {
    val map = mapOf(
      "someValue" to mapOf(
        "type" to "Command",
        "value" to ";",
      ),
    )
    shouldThrow<IllegalArgumentException> { objectMapper.convertValue<Config>(map) }
  }
}
