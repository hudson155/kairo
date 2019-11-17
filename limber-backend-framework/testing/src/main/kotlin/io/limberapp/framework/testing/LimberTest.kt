package io.limberapp.framework.testing

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationCall
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import io.limberapp.framework.LimberApp
import io.limberapp.framework.endpoint.EndpointConfig
import io.limberapp.framework.endpoint.authorization.jwt.Jwt
import io.limberapp.framework.endpoint.authorization.jwt.JwtRole
import io.limberapp.framework.endpoint.authorization.jwt.JwtUser
import io.limberapp.framework.endpoint.authorization.jwt.withJwt
import io.limberapp.framework.jackson.objectMapper.LimberObjectMapper
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private fun withLimberTestApp(limberApp: LimberApp<*>, test: TestApplicationEngine.() -> Unit) {
    withTestApplication({ limberApp.bindToApplication(this) }, test)
}

class LimberTest(private val limberApp: TestLimberApp) {

    private val objectMapper = LimberObjectMapper()

    @Suppress("LongParameterList") // For this test method, we're ok with it.
    fun test(
        endpointConfig: EndpointConfig,
        pathParams: Map<String, String> = emptyMap(),
        queryParams: Map<String, String> = emptyMap(),
        body: Any? = null,
        expectedStatusCode: HttpStatusCode = HttpStatusCode.OK,
        test: TestApplicationCall.() -> Unit
    ) = withLimberTestApp(limberApp) {
        createCall(endpointConfig, pathParams, queryParams, body)
            .runTest(expectedStatusCode, test)
    }

    private fun TestApplicationEngine.createCall(
        endpointConfig: EndpointConfig,
        pathParams: Map<String, String>,
        queryParams: Map<String, String>,
        body: Any?
    ): TestApplicationCall {
        return handleRequest(
            method = endpointConfig.httpMethod,
            uri = endpointConfig.path(pathParams, queryParams)
        ) {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            val jwt = JWT.create().withJwt(
                jwt = Jwt(
                    orgs = emptyMap(),
                    roles = setOf(JwtRole.SUPERUSER),
                    user = JwtUser(UUID.randomUUID(), "Jeff Hudson")
                )
            ).sign(Algorithm.none())
            addHeader(HttpHeaders.Authorization, "Bearer $jwt")
            body?.let { setBody(objectMapper.writeValueAsString(it)) }
        }
    }

    private fun TestApplicationCall.runTest(
        expectedStatusCode: HttpStatusCode,
        test: TestApplicationCall.() -> Unit
    ) {
        assertTrue(
            actual = requestHandled,
            message = "The HTTP request was not handled." +
                    " Is the path wrong or did you forget to register the ApiEndpoint?"
        )
        assertEquals(expectedStatusCode, response.status(), "Unexpected HTTP response code.")
        test()
    }
}
