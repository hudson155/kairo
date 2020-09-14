package io.limberapp.common.testing

import io.ktor.application.Application
import io.ktor.application.ApplicationStarted
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.server.testing.TestApplicationCall
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.createTestEnvironment
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.limberapp.common.error.LimberError
import io.limberapp.common.exception.LimberException
import io.limberapp.common.exceptionMapping.ExceptionMapper
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.common.restInterface.forKtor
import io.limberapp.common.serialization.Json
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@Suppress("LongParameterList") // For these methods, we're ok with it.
abstract class LimberTest(
  protected val json: Json,
  private val moduleFunction: Application.() -> Unit,
) {
  private val exceptionMapper = ExceptionMapper()

  fun setup(endpoint: LimberEndpoint) = testInternal(
    endpoint = endpoint,
    expectedStatusCode = HttpStatusCode.OK,
    test = {}
  )

  fun test(
    endpoint: LimberEndpoint,
    expectedStatusCode: HttpStatusCode = HttpStatusCode.OK,
    test: TestApplicationCall.() -> Unit,
  ) = testInternal(
    endpoint = endpoint,
    expectedStatusCode = expectedStatusCode,
    test = test
  )

  fun test(endpoint: LimberEndpoint, expectedException: LimberException) {
    val expectedError = exceptionMapper.handle(expectedException)
    testInternal(
      endpoint = endpoint,
      expectedStatusCode = HttpStatusCode.fromValue(expectedError.statusCode),
      test = {
        val actual = json.parse<LimberError>(response.content!!)
        assertEquals(expectedError, actual)
      }
    )
  }

  private fun testInternal(
    endpoint: LimberEndpoint,
    expectedStatusCode: HttpStatusCode,
    test: TestApplicationCall.() -> Unit,
  ) = withLimberTestApp {
    createCall(endpoint)
      .runTest(expectedStatusCode, test)
  }

  private val engine = TestApplicationEngine(createTestEnvironment())

  fun start() {
    engine.start()
    engine.doOrStop {
      application.moduleFunction()
      // TestApplicationEngine does not raise ApplicationStarted.
      environment.monitor.raise(ApplicationStarted, application)
    }
  }

  fun stop() {
    @Suppress("MagicNumber")
    engine.stop(3000, 5000)
  }

  private fun withLimberTestApp(test: TestApplicationEngine.() -> Unit) {
    engine.doOrStop { test() }
  }

  private fun TestApplicationEngine.doOrStop(function: TestApplicationEngine.() -> Unit) {
    @Suppress("TooGenericExceptionCaught")
    try {
      function()
    } catch (e: Throwable) {
      stop()
      throw e
    }
  }

  private fun TestApplicationEngine.createCall(endpoint: LimberEndpoint): TestApplicationCall {
    return handleRequest(endpoint.httpMethod.forKtor(), endpoint.href) {
      createAuthHeader()?.let { addHeader(HttpHeaders.Authorization, it.toString()) }
      addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
      addHeader(HttpHeaders.Accept, endpoint.contentType.forKtor().toString())
      endpoint.body?.let { setBody(json.stringify(it)) }
    }
  }

  protected abstract fun createAuthHeader(): HttpAuthHeader?

  private fun TestApplicationCall.runTest(
    expectedStatusCode: HttpStatusCode,
    test: TestApplicationCall.() -> Unit,
  ) {
    assertTrue(
      actual = requestHandled,
      message = "The HTTP request was not handled." +
        " Is the path wrong or did you forget to register the ApiEndpoint?"
    )
    assertEquals(
      expectedStatusCode,
      response.status(),
      "Unexpected HTTP response code.\nResponse: ${response.content}\n"
    )
    test()
  }
}

val TestApplicationCall.responseContent get() = assertNotNull(response.content)
