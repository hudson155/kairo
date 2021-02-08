package io.limberapp.testing.integration

import io.ktor.application.Application
import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.common.client.exception.LimberHttpClientException
import io.limberapp.common.config.ConfigLoader
import io.limberapp.common.exception.badRequest.RankOutOfBounds
import io.limberapp.common.exception.forbidden.ForbiddenException
import io.limberapp.common.exception.unauthorized.UnauthorizedException
import io.limberapp.common.module.Module
import io.limberapp.common.server.Server
import io.limberapp.testing.integration.IntegrationTest.Extension
import kotlinx.coroutines.delay
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/**
 * This test serves 2 purposes. Firstly, it proves that the [AbstractIntegrationTest] and
 * [AbstractIntegrationTestExtension] work properly. If this test fails on startup, one of those is
 * likely to blame. Secondly, it tests a few of the properties and methods on
 * [AbstractIntegrationTest] (see individual test cases).
 *
 * The test cases are split up into multiple inner classes. The reason for this is because
 * [Extension] is rigged to fail if server startup is attempted multiple times. By using multiple
 * inner classes, we're able to ensure that even across multiple test classes, the server only
 * starts once.
 */
@Suppress("BlockingMethodInNonBlockingContext")
internal class IntegrationTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : AbstractIntegrationTest(engine, server) {
  internal class Extension : AbstractIntegrationTestExtension() {
    companion object {
      var serverStartupCount: Int = 0
    }

    override fun Application.main(): Server<TestConfig> {
      assertEquals(0, serverStartupCount)
      serverStartupCount++
      return object : Server<TestConfig>(
          application = this,
          config = ConfigLoader.load("test"),
      ) {
        override val modules: Set<Module> = emptySet()
      }
    }
  }

  @ExtendWith(Extension::class)
  internal class Metadata(
      engine: TestApplicationEngine,
      server: Server<*>,
  ) : AbstractIntegrationTest(engine, server) {
    @Test
    fun `The HTTP client should use the right implementation`() {
      assertTrue(httpClient is IntegrationTestHttpClient)
    }

    @Test
    fun `Tests should have access to a fresh UUID generator`() {
      val guid1 = guids.generate()
      guids.reset()
      val guid2 = guids.generate()
      assertEquals(guid1, guid2)
    }

    @Test
    fun `Tests should have access to a fixed clock`() {
      val expected = ZonedDateTime.of(2007, 12, 3, 5, 15, 30, 789_000_000,
          ZoneId.of("America/New_York"))
      assertEquals(expected, ZonedDateTime.now(clock))
    }
  }

  @ExtendWith(Extension::class)
  internal class Methods(
      engine: TestApplicationEngine,
      server: Server<*>,
  ) : AbstractIntegrationTest(engine, server) {
    @Test
    fun `Setup method`() {
      var called = false
      setup {
        called = true
        delay(0) // Ensures suspend functions are allowed.
      }
      assertTrue(called)
    }

    @Test
    fun `Test (result) method, happy path`() {
      var called = false
      test(expectResult = 5) {
        called = true
        delay(0) // Ensures suspend functions are allowed.
        return@test 5
      }
      assertTrue(called)
    }

    @Test
    fun `Test (result) method, failure`() {
      var called = false
      assertFailsWith<AssertionError> {
        test(expectResult = 5) {
          called = true
          delay(0) // Ensures suspend functions are allowed.
          return@test 2
        }
      }.let { e ->
        assertEquals("expected: <5> but was: <2>", e.message)
      }
      assertTrue(called)
    }

    @Test
    fun `Test (error) method, happy path`() {
      var called = false
      test(expectError = RankOutOfBounds(5)) {
        called = true
        delay(0) // Ensures suspend functions are allowed.
        val e = RankOutOfBounds(5)
        throw LimberHttpClientException(e.statusCode, objectMapper.writeValueAsString(e.properties))
      }
      assertTrue(called)
    }

    @Test
    fun `Test (error) method, failure by no error`() {
      var called = false
      assertFailsWith<AssertionError> {
        test(expectError = RankOutOfBounds(5)) {
          called = true
          delay(0) // Ensures suspend functions are allowed.
          return@test 2
        }
      }.let { e ->
        assertEquals("Expected exception. Instead got 2.", e.message)
      }
      assertTrue(called)
    }

    @Test
    fun `Test (error) method, failure by incorrect error`() {
      var called = false
      assertFailsWith<AssertionError> {
        test(expectError = ForbiddenException()) {
          called = true
          delay(0) // Ensures suspend functions are allowed.
          val e = UnauthorizedException()
          throw LimberHttpClientException(e.statusCode,
              objectMapper.writeValueAsString(e.properties))
        }
      }.let { e ->
        assertEquals(
            expected = "expected: <{" +
                "error=ForbiddenException," +
                " statusCode=403," +
                " statusCodeDescription=Forbidden," +
                " message=Forbidden." +
                "}> but was: <{" +
                "error=UnauthorizedException," +
                " message=Unauthorized.," +
                " statusCode=401," +
                " statusCodeDescription=Unauthorized" +
                "}>",
            actual = e.message,
        )
      }
      assertTrue(called)
    }
  }
}
