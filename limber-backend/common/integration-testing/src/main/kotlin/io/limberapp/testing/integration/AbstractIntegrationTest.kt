package io.limberapp.testing.integration

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Key
import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.client.HttpClient
import io.limberapp.client.exception.LimberHttpClientException
import io.limberapp.exception.LimberException
import io.limberapp.module.typeLiteral
import io.limberapp.serialization.LimberObjectMapper
import io.limberapp.server.Server
import io.limberapp.util.uuid.DeterministicUuidGenerator
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Clock
import kotlin.reflect.KClass
import kotlin.test.assertEquals
import kotlin.test.fail

@ExtendWith(TestApplicationEngineParameterResolver::class)
abstract class AbstractIntegrationTest(
    engine: TestApplicationEngine,
    private val server: Server<*>,
) {
  inner class LimberIntegrationTestMocks {
    operator fun <T : Any> get(key: KClass<T>): T = server.injector.getInstance(key.java)
  }

  protected val objectMapper: LimberObjectMapper = LimberObjectMapper(
      prettyPrint = true,
      typeConverters = server.injector.getInstance(Key.get(typeLiteral())),
  )
  protected val httpClient: HttpClient = IntegrationTestHttpClient(engine, objectMapper)

  protected val mocks: LimberIntegrationTestMocks = LimberIntegrationTestMocks()

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
