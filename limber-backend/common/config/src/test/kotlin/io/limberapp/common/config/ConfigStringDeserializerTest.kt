package io.limberapp.common.config

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.module.kotlin.convertValue
import io.limberapp.common.serialization.LimberObjectMapper
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class ConfigStringDeserializerTest {
  private val objectMapper: LimberObjectMapper = LimberObjectMapper()

  data class Plaintext(
      @JsonDeserialize(using = ConfigStringDeserializer::class)
      val someValue: String,
  )

  @Test
  fun usedIncorrectly() {
    val map = mapOf("someValue" to "the value!")
    assertFailsWith<IllegalArgumentException> { objectMapper.convertValue<Plaintext>(map) }
  }

  @Test
  fun plaintext() {
    val map = mapOf("someValue" to mapOf("type" to "PLAINTEXT", "value" to "the value!"))
    val result = objectMapper.convertValue<Plaintext>(map)
    assertEquals("the value!", result.someValue)
  }

  @Test
  fun `environmentVariable - value is set`() {
    withEnvironmentVariable("LIMBER_TEST_ENV_VAR_1", "val from env!") {
      val map = mapOf("someValue" to mapOf(
          "type" to "ENVIRONMENT_VARIABLE",
          "name" to "LIMBER_TEST_ENV_VAR_1",
          "defaultValue" to "this is unused",
      ))
      val result = objectMapper.convertValue<Plaintext>(map)
      assertEquals("val from env!", result.someValue)
    }
  }

  @Test
  fun `environmentVariable - value not set, no default`() {
    withEnvironmentVariable("LIMBER_TEST_ENV_VAR_2", "this is unused") {
      val map = mapOf("someValue" to mapOf(
          "type" to "ENVIRONMENT_VARIABLE",
          "name" to "LIMBER_TEST_ENV_VAR_1",
      ))
      assertFailsWith<IllegalArgumentException> { objectMapper.convertValue<Plaintext>(map) }
    }
  }

  @Test
  fun `environmentVariable - value not set, has default`() {
    withEnvironmentVariable("LIMBER_TEST_ENV_VAR_2", "this is unused") {
      val map = mapOf("someValue" to mapOf(
          "type" to "ENVIRONMENT_VARIABLE",
          "name" to "LIMBER_TEST_ENV_VAR_1",
          "defaultValue" to "I guess we get the default",
      ))
      val result = objectMapper.convertValue<Plaintext>(map)
      assertEquals("I guess we get the default", result.someValue)
    }
  }

  @OptIn(EnvironmentManipulation::class)
  private fun withEnvironmentVariable(name: String, value: String, function: () -> Unit) {
    try {
      getEnv = { if (it == name) value else System.getenv(it) }
      function()
    } finally {
      getEnv = System::getenv
    }
  }
}
