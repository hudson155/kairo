package io.limberapp.common.config

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class ConfigLoaderTest {
  @Test
  fun `valid config`() {
    val config = ConfigLoader.load<TestConfig>("valid-test-config")
    assertEquals(TestConfig(
        authentication = AuthenticationConfig(
            mechanisms = listOf(
                AuthenticationMechanism.Jwt.Signed(
                    issuer = "https://limberapp.io/",
                    leeway = 20L,
                    algorithm = AuthenticationMechanism.Jwt.Signed.Algorithm.HMAC256,
                    secret = "supersecret",
                ),
                AuthenticationMechanism.Jwk(
                    issuer = "https://limber.auth0.com/",
                    leeway = 20L,
                    domain = "limber.auth0.com",
                ),
            ),
        ),
        clock = ClockConfig(type = ClockConfig.Type.REAL),
        customProperty = "custom value",
        hosts = TestHosts("http://limber-something-server"),
        uuids = UuidsConfig(generation = UuidsConfig.Generation.RANDOM),
    ), config)
  }

  @Test
  fun `missing config`() {
    assertFailsWith<IllegalArgumentException> {
      ConfigLoader.load<TestConfig>("missing-config")
    }.let { e ->
      assertEquals("resource config/missing-config.yaml not found.", e.message)
    }
  }

  @Test
  fun `extra config property`() {
    assertFailsWith<UnrecognizedPropertyException> {
      ConfigLoader.load<TestConfig>("config-with-extra-property")
    }.let { e ->
      val message = assertNotNull(e.message)
      assertTrue(message.startsWith("Unrecognized field \"customProperty2\""),
          message = message)
    }
  }

  @Test
  fun `missing config property`() {
    assertFailsWith<MissingKotlinParameterException> {
      ConfigLoader.load<TestConfig>("config-with-missing-property")
    }.let { e ->
      val message = assertNotNull(e.message)
      assertTrue(message.contains("missing (therefore NULL) value for creator parameter clock"),
          message = message)
    }
  }

  @Test
  fun `invalid config property (wrong type)`() {
    assertFailsWith<MismatchedInputException> {
      ConfigLoader.load<TestConfig>("config-with-invalid-property")
    }.let { e ->
      val message = assertNotNull(e.message)
      assertTrue(message.startsWith("Cannot deserialize instance of"),
          message = message)
    }
  }

  @Test
  fun `JSON config`() {
    assertFailsWith<IllegalArgumentException> {
      ConfigLoader.load<TestConfig>("valid-test-json-config")
    }.let { e ->
      assertEquals("resource config/valid-test-json-config.yaml not found.", e.message)
    }
  }
}
