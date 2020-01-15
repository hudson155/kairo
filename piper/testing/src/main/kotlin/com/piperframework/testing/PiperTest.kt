package com.piperframework.testing

import com.fasterxml.jackson.module.kotlin.readValue
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.error.PiperError
import com.piperframework.exception.PiperException
import com.piperframework.exceptionMapping.ExceptionMapper
import com.piperframework.jackson.objectMapper.PiperObjectMapper
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
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("LongParameterList") // For these methods, we're ok with it.
abstract class PiperTest(private val moduleFunction: Application.() -> Unit) {

    protected val objectMapper = PiperObjectMapper()

    private val exceptionMapper = ExceptionMapper()

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
                val actual = objectMapper.readValue<PiperError>(response.content!!)
                assertEquals(expectedError, actual)
            }
        )
    }

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

    private val engine = TestApplicationEngine(createTestEnvironment())

    fun start() {
        engine.start()
        try {
            engine.application.moduleFunction()
            // TestApplicationEngine does not raise ApplicationStarted.
            engine.environment.monitor.raise(ApplicationStarted, engine.application)
        } catch (e: Throwable) {
            stop()
            throw e
        }
    }

    fun stop() {
        engine.stop(0L, 0L, TimeUnit.MILLISECONDS)
    }

    private fun withPiperTestApp(test: TestApplicationEngine.() -> Unit) {
        try {
            engine.test()
        } catch (e: Throwable) {
            engine.stop(0L, 0L, TimeUnit.MILLISECONDS)
            throw e
        }
    }

    private fun TestApplicationEngine.createCall(
        endpointConfig: EndpointConfig,
        pathParams: Map<String, String>,
        queryParams: Map<String, String>,
        body: Any?
    ): TestApplicationCall {
        return handleRequest(endpointConfig.httpMethod, endpointConfig.path(pathParams, queryParams)) {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            createAuthHeader()?.let { addHeader(HttpHeaders.Authorization, it.toString()) }
            body?.let { setBody(objectMapper.writeValueAsString(it)) }
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
