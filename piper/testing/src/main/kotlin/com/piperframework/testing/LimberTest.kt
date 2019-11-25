package com.piperframework.testing

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationCall
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import com.piperframework.LimberApp
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.jackson.objectMapper.LimberObjectMapper
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private fun withLimberTestApp(limberApp: LimberApp<*>, test: TestApplicationEngine.() -> Unit) {
    withTestApplication({ limberApp.bindToApplication(this) }, test)
}

abstract class LimberTest(private val limberApp: TestLimberApp) {

    protected val objectMapper = LimberObjectMapper()

    @Suppress("LongParameterList") // For this test method, we're ok with it.
    fun test(
        endpointConfig: com.piperframework.endpoint.EndpointConfig,
        pathParams: Map<String, String> = emptyMap(),
        queryParams: Map<String, String> = emptyMap(),
        body: Any? = null,
        expectedStatusCode: HttpStatusCode = HttpStatusCode.OK,
        test: TestApplicationCall.() -> Unit
    ) = withLimberTestApp(limberApp) {
        createCall(endpointConfig, pathParams, queryParams, body).runTest(expectedStatusCode, test)
    }

    private fun TestApplicationEngine.createCall(
        endpointConfig: com.piperframework.endpoint.EndpointConfig,
        pathParams: Map<String, String>,
        queryParams: Map<String, String>,
        body: Any?
    ): TestApplicationCall {
        return handleRequest(endpointConfig.httpMethod, endpointConfig.path(pathParams, queryParams)) {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            val jwt = createJwt()
            addHeader(HttpHeaders.Authorization, "Bearer $jwt")
            body?.let { setBody(objectMapper.writeValueAsString(it)) }
        }
    }

    protected abstract fun createJwt(): String

    private fun TestApplicationCall.runTest(expectedStatusCode: HttpStatusCode, test: TestApplicationCall.() -> Unit) {
        assertTrue(
            actual = requestHandled,
            message = "The HTTP request was not handled." +
                    " Is the path wrong or did you forget to register the ApiEndpoint?"
        )
        assertEquals(expectedStatusCode, response.status(), "Unexpected HTTP response code.")
        test()
    }
}
