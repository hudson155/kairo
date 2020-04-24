package com.piperframework.testing

import com.piperframework.error.PiperError
import com.piperframework.exception.PiperException
import com.piperframework.exceptionMapping.ExceptionMapper
import com.piperframework.restInterface.PiperEndpoint
import com.piperframework.restInterface.forKtor
import com.piperframework.serialization.Json
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
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("LongParameterList") // For these methods, we're ok with it.
abstract class PiperTest(
    protected val json: Json,
    private val moduleFunction: Application.() -> Unit
) {
    private val exceptionMapper = ExceptionMapper()

    fun setup(endpoint: PiperEndpoint) = testInternal(
        endpoint = endpoint,
        expectedStatusCode = HttpStatusCode.OK,
        test = {}
    )

    fun test(
        endpoint: PiperEndpoint,
        expectedStatusCode: HttpStatusCode = HttpStatusCode.OK,
        test: TestApplicationCall.() -> Unit
    ) = testInternal(
        endpoint = endpoint,
        expectedStatusCode = expectedStatusCode,
        test = test
    )

    fun test(endpoint: PiperEndpoint, expectedException: PiperException) {
        val expectedError = exceptionMapper.handle(expectedException)
        testInternal(
            endpoint = endpoint,
            expectedStatusCode = HttpStatusCode.fromValue(expectedError.statusCode),
            test = {
                val actual = json.parse<PiperError>(response.content!!)
                assertEquals(expectedError, actual)
            }
        )
    }

    private fun testInternal(
        endpoint: PiperEndpoint,
        expectedStatusCode: HttpStatusCode,
        test: TestApplicationCall.() -> Unit
    ) = withPiperTestApp {
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
        engine.stop(0L, 0L)
    }

    private fun withPiperTestApp(test: TestApplicationEngine.() -> Unit) {
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

    private fun TestApplicationEngine.createCall(endpoint: PiperEndpoint): TestApplicationCall {
        return handleRequest(endpoint.httpMethod.forKtor(), endpoint.href) {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            createAuthHeader()?.let { addHeader(HttpHeaders.Authorization, it.toString()) }
            endpoint.body?.let { setBody(json.stringify(it)) }
        }
    }

    protected abstract fun createAuthHeader(): HttpAuthHeader?

    private fun TestApplicationCall.runTest(
        expectedStatusCode: HttpStatusCode,
        test: TestApplicationCall.() -> Unit
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
