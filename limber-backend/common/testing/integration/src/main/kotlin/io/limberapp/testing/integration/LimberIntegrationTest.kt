package io.limberapp.testing.integration

import io.ktor.server.testing.TestApplicationCall
import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.common.LimberApplication
import io.limberapp.common.client.LimberHttpClient
import io.limberapp.common.client.exception.LimberHttpClientException
import io.limberapp.common.exception.ExceptionMapper
import io.limberapp.common.exception.LimberException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.reflect.KClass
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.fail

@ExtendWith(TestApplicationEngineParameterResolver::class)
abstract class LimberIntegrationTest(
    engine: TestApplicationEngine,
    private val limberServer: LimberApplication<*>,
) {
  inner class LimberIntegrationTestMocks {
    operator fun <T : Any> get(key: KClass<T>): T = limberServer.injector.getInstance(key.java)
  }

  protected val httpClient: LimberHttpClient = IntegrationTestHttpClient(engine)

  protected val mocks = LimberIntegrationTestMocks()

  val uuidGenerator get() = limberServer.uuidGenerator

  val clock get() = limberServer.clock

  protected val TestApplicationCall.responseContent get() = assertNotNull(response.content)

  protected fun setup(block: suspend () -> Unit) = runBlocking {
    block()
  }

  protected fun <T> test(expectResult: T, block: suspend () -> T) = runBlocking {
    val actual = block()
    assertEquals(expectResult, actual)
  }

  protected fun test(expectError: LimberException, block: suspend () -> Any?) = runBlocking {
    try {
      val actual = block()
      fail("Expected exception. Instead got $actual.")
    } catch (e: LimberHttpClientException) {
      assertEquals(ExceptionMapper.handle(expectError), e.error)
    }
  }
}
