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
import io.limberapp.framework.endpoint.ApiEndpoint
import io.limberapp.framework.endpoint.authorization.jwt.Jwt
import io.limberapp.framework.endpoint.authorization.jwt.JwtUser
import io.limberapp.framework.endpoint.authorization.jwt.withJwt
import io.limberapp.framework.jackson.objectMapper.LimberObjectMapper
import io.limberapp.framework.rep.CreationRep
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private fun withLimberTestApp(limberApp: LimberApp, test: TestApplicationEngine.() -> Unit) {
    withTestApplication({ limberApp.bindToApplication(this) }, test)
}

abstract class AbstractResourceTest {

    protected abstract val limberTest: LimberTest

    protected val objectMapper = LimberObjectMapper(prettyPrint = false)

    protected inner class LimberTest(private val limberApp: LimberApp) {

        fun <T : CreationRep> post(
            config: ApiEndpoint.Config,
            pathParams: Map<String, String> = emptyMap(),
            body: T,
            test: TestApplicationCall.() -> Unit
        ) = withLimberTestApp(limberApp) {
            val call = handleRequest(config.httpMethod, config.path(pathParams)) {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                val jwt = JWT.create().withJwt(
                    Jwt(orgs = emptyMap(), user = JwtUser(UUID.randomUUID()))
                ).sign(Algorithm.none())
                addHeader(HttpHeaders.Authorization, "Bearer $jwt")
                setBody(objectMapper.writeValueAsString(body))
            }
            with(call) {
                assertTrue(requestHandled)
                assertEquals(HttpStatusCode.OK, response.status())
                test()
            }
        }
    }
}

