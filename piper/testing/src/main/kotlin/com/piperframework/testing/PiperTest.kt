package com.piperframework.testing

import com.piperframework.endpoint.EndpointConfig
import com.piperframework.error.PiperError
import com.piperframework.exception.PiperException
import com.piperframework.exceptionMapping.ExceptionMapper
import com.piperframework.restInterface.PiperEndpoint
import com.piperframework.serialization.Json
import com.piperframework.util.forKtor
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

    @Deprecated("API Transition")
    fun setup(
        endpointConfig: EndpointConfig,
        pathParams: Map<String, Any> = emptyMap(),
        queryParams: Map<String, Any> = emptyMap(),
        body: Any? = null
    ) = testInternal(
        endpointConfig = endpointConfig,
        pathParams = pathParams,
        queryParams = queryParams,
        body = body,
        expectedStatusCode = HttpStatusCode.OK,
        test = {}
    )

    fun setup(endpointConfig: PiperEndpoint) = testInternal(
        endpointConfig = endpointConfig,
        expectedStatusCode = HttpStatusCode.OK,
        test = {}
    )

    @Deprecated("API Transition")
    fun test(
        endpointConfig: EndpointConfig,
        pathParams: Map<String, Any> = emptyMap(),
        queryParams: Map<String, Any> = emptyMap(),
        body: Any? = null,
        expectedStatusCode: HttpStatusCode = HttpStatusCode.OK,
        test: TestApplicationCall.() -> Unit
    ) = testInternal(
        endpointConfig = endpointConfig,
        pathParams = pathParams,
        queryParams = queryParams,
        body = body,
        expectedStatusCode = expectedStatusCode,
        test = test
    )

    fun test(
        endpointConfig: PiperEndpoint,
        expectedStatusCode: HttpStatusCode = HttpStatusCode.OK,
        test: TestApplicationCall.() -> Unit
    ) = testInternal(
        endpointConfig = endpointConfig,
        expectedStatusCode = expectedStatusCode,
        test = test
    )

    @Deprecated("API Transition")
    fun test(
        endpointConfig: EndpointConfig,
        pathParams: Map<String, Any> = emptyMap(),
        queryParams: Map<String, Any> = emptyMap(),
        body: Any? = null,
        expectedException: PiperException
    ) {
        val expectedError = exceptionMapper.handle(expectedException)
        testInternal(
            endpointConfig = endpointConfig,
            pathParams = pathParams,
            queryParams = queryParams,
            body = body,
            expectedStatusCode = HttpStatusCode.fromValue(expectedError.statusCode),
            test = {
                val actual = json.parse<PiperError>(response.content!!)
                assertEquals(expectedError, actual)
            }
        )
    }

    fun test(endpoint: PiperEndpoint, expectedException: PiperException) {
        val expectedError = exceptionMapper.handle(expectedException)
        testInternal(
            endpointConfig = endpoint,
            expectedStatusCode = HttpStatusCode.fromValue(expectedError.statusCode),
            test = {
                val actual = json.parse<PiperError>(response.content!!)
                assertEquals(expectedError, actual)
            }
        )
    }

    @Deprecated("API Transition")
    private fun testInternal(
        endpointConfig: EndpointConfig,
        pathParams: Map<String, Any>,
        queryParams: Map<String, Any>,
        body: Any?,
        expectedStatusCode: HttpStatusCode,
        test: TestApplicationCall.() -> Unit
    ) = withPiperTestApp {
        createCall(
            endpointConfig = endpointConfig,
            pathParams = pathParams.mapValues { it.value.toString() },
            queryParams = queryParams.mapValues { it.value.toString() },
            body = body
        ).runTest(expectedStatusCode, test)
    }

    private fun testInternal(
        endpointConfig: PiperEndpoint,
        expectedStatusCode: HttpStatusCode,
        test: TestApplicationCall.() -> Unit
    ) = withPiperTestApp {
        createCall(endpointConfig)
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

    @Deprecated("API Transition")
    private fun TestApplicationEngine.createCall(
        endpointConfig: EndpointConfig,
        pathParams: Map<String, String>,
        queryParams: Map<String, String>,
        body: Any?
    ): TestApplicationCall {
        return handleRequest(endpointConfig.httpMethod, endpointConfig.path(pathParams, queryParams)) {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            createAuthHeader()?.let { addHeader(HttpHeaders.Authorization, it.toString()) }
            body?.let { setBody(json.stringify(it)) }
        }
    }

    private fun TestApplicationEngine.createCall(endpointConfig: PiperEndpoint): TestApplicationCall {
        return handleRequest(endpointConfig.httpMethod.forKtor(), endpointConfig.href) {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            createAuthHeader()?.let { addHeader(HttpHeaders.Authorization, it.toString()) }
            endpointConfig.body?.let { setBody(json.stringify(it)) }
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
