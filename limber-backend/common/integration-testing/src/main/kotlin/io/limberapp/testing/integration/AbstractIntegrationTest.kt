package io.limberapp.testing.integration

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.common.client.HttpClient
import io.limberapp.common.client.exception.LimberHttpClientException
import io.limberapp.common.exception.LimberException
import io.limberapp.common.serialization.LimberObjectMapper
import io.limberapp.common.server.Server
import io.limberapp.common.util.uuid.DeterministicUuidGenerator
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Clock
import kotlin.test.assertEquals
import kotlin.test.fail

@ExtendWith(TestApplicationEngineParameterResolver::class)
abstract class AbstractIntegrationTest(
    engine: TestApplicationEngine,
    private val server: Server<*>,
) {
  protected val objectMapper: LimberObjectMapper = LimberObjectMapper(prettyPrint = true)
  protected val httpClient: HttpClient = IntegrationTestHttpClient(engine)

  val guids: DeterministicUuidGenerator get() = server.uuidGenerator
  val clock: Clock get() = server.clock

  protected fun setup(block: suspend () -> Unit): Unit = runBlocking {
    block()
  }

  protected fun <T> test(expectResult: T, block: suspend () -> T): Unit = runBlocking {
    val actual = block()
    assertEquals(expectResult, actual)
  }

  protected fun test(expectError: LimberException, block: suspend () -> Any?): Unit = runBlocking {
    try {
      val actual = block()
      fail("Expected exception. Instead got $actual.")
    } catch (e: LimberHttpClientException) {
      assertEquals(expectError.properties, objectMapper.readValue(e.errorMessage))
    }
  }
}
