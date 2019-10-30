package io.limberapp.backend.module.orgs.endpoint.org

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.application.Application
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationCall
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import io.limberapp.backend.module.orgs.OrgsModule
import io.limberapp.backend.module.orgs.endpoint.TestMainModule
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.framework.LimberApp
import io.limberapp.framework.config.Config
import io.limberapp.framework.config.database.DatabaseConfig
import io.limberapp.framework.config.jwt.JwtConfig
import io.limberapp.framework.endpoint.ApiEndpoint
import io.limberapp.framework.endpoint.authorization.jwt.Jwt
import io.limberapp.framework.endpoint.authorization.jwt.JwtUser
import io.limberapp.framework.endpoint.authorization.jwt.withJwt
import io.limberapp.framework.jackson.objectMapper.LimberObjectMapper
import io.limberapp.framework.rep.CreationRep
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private val objectMapper = LimberObjectMapper(prettyPrint = false)

abstract class ResourceTest {

    protected object LimberTest {

        fun <T : CreationRep> post(
            config: ApiEndpoint.Config,
            pathParams: Map<String, String> = emptyMap(),
            body: T,
            test: TestApplicationCall.() -> Unit
        ) = withLimberTestApp {
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
            }
        }
    }
}

private fun withLimberTestApp(any: TestApplicationEngine.() -> Unit) {
    withTestApplication({
        val config = Config(
            database = DatabaseConfig(
                host = "cluster0-pct9z.gcp.mongodb.net",
                database = "limberapptest",
                user = "limberapp",
                password = "kalcDr1P4!vE"
            ),
            jwt = JwtConfig(requireSignature = false)
        )
        object : LimberApp(config) {
            override fun getMainModule(application: Application) =
                TestMainModule(application, config)

            override val modules = listOf(OrgsModule())
        }.bindToApplication(this)
    }, any)
}

class CreateOrgTest : ResourceTest() {

    @Test
    fun ok() = LimberTest.post(
        config = CreateOrg.config,
        body = OrgRep.Creation("Cranky Pasta")
    ) {
        val actual = objectMapper.readValue<OrgRep.Complete>(response.content!!)
        val expected = OrgRep.Complete(actual.id, actual.created, 0, "Cranky Pasta")
        assertEquals(expected, actual)
    }
}
