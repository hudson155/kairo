package limber.config.deserializer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.module.kotlin.convertValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import limber.serialization.ObjectMapperFactory
import org.junit.jupiter.api.Test

internal class ConfigStringDeserializerTest {
  private val objectMapper: ObjectMapper = ObjectMapperFactory.builder(ObjectMapperFactory.Format.JSON).build()

  data class Config(
    @JsonDeserialize(using = ConfigStringDeserializer::class)
    val someValue: String?,
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
    objectMapper.convertValue<Config>(map).someValue.shouldBe("the value")
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
          "name" to "TEST_ENV_VAR",
        ),
      )
      objectMapper.convertValue<Config>(map).someValue.shouldBe("val from env")
    }
  }

  @Test
  fun `environment variable - value not set, has default`() {
    EnvironmentVariableSource.withOverride({ null }) {
      val map = mapOf(
        "someValue" to mapOf(
          "type" to "EnvironmentVariable",
          "name" to "TEST_ENV_VAR",
          "defaultValue" to "some default",
        ),
      )
      objectMapper.convertValue<Config>(map).someValue.shouldBe("some default")
    }
  }

  @Test
  fun `environment variable - value not set, no default`() {
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
  fun `gcp secret`() {
    val map = mapOf(
      "someValue" to mapOf(
        "type" to "GcpSecret",
        "environmentVariableName" to "TEST_ENV_VAR",
      ),
    )
    shouldThrow<IllegalArgumentException> { objectMapper.convertValue<Config>(map) }
  }

  @Test
  fun command() {
    val map = mapOf(
      "someValue" to mapOf(
        "type" to "Command",
        "command" to "echo \"val from cmd\"",
      ),
    )
    shouldThrow<IllegalArgumentException> { objectMapper.convertValue<Config>(map) }
  }
}
