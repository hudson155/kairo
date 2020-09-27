package io.limberapp.testing.integration

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.server.testing.TestApplicationCall
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.limberapp.backend.authorization.principal.Claims
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.authorization.principal.JwtUser
import io.limberapp.common.LimberApplication
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.common.serialization.Json
import io.limberapp.error.LimberError
import io.limberapp.exception.LimberException
import io.limberapp.exceptionMapping.ExceptionMapper
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*
import kotlin.reflect.KClass
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExtendWith(TestApplicationEngineParameterResolver::class)
abstract class LimberIntegrationTest(
  private val engine: TestApplicationEngine,
  private val limberServer: LimberApplication<*>,
) {
  inner class LimberIntegrationTestMocks {
    operator fun <T : Any> get(key: KClass<T>): T = limberServer.injector.getInstance(key.java)
  }

  protected val json = Json(prettyPrint = true)

  protected val mocks = LimberIntegrationTestMocks()

  val uuidGenerator get() = limberServer.uuidGenerator

  val clock get() = limberServer.clock

  protected val TestApplicationCall.responseContent get() = assertNotNull(response.content)

  protected fun setup(endpoint: LimberEndpoint) = makeServerCall(
    endpoint = endpoint,
    expectedStatusCode = HttpStatusCode.OK,
    block = {},
  )

  protected fun test(
    endpoint: LimberEndpoint,
    expectedStatusCode: HttpStatusCode = HttpStatusCode.OK,
    test: TestApplicationCall.() -> Unit,
  ) = makeServerCall(
    endpoint = endpoint,
    expectedStatusCode = expectedStatusCode,
    block = test,
  )

  protected fun test(endpoint: LimberEndpoint, expectedException: LimberException) {
    val expectedError = ExceptionMapper.handle(expectedException)
    makeServerCall(
      endpoint = endpoint,
      expectedStatusCode = HttpStatusCode.fromValue(expectedError.statusCode),
      block = {
        val actual = json.parse<LimberError>(checkNotNull(response.content))
        assertEquals(expectedError, actual)
      },
    )
  }

  private fun makeServerCall(
    endpoint: LimberEndpoint,
    expectedStatusCode: HttpStatusCode,
    block: TestApplicationCall.() -> Unit,
  ) {
    engine.createCall(endpoint)
      .runTest(expectedStatusCode, block)
  }

  private fun TestApplicationEngine.createCall(endpoint: LimberEndpoint): TestApplicationCall {
    return handleRequest(endpoint.httpMethod, endpoint.href) {
      createAuthHeader()?.let { addHeader(HttpHeaders.Authorization, it.toString()) }
      addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
      addHeader(HttpHeaders.Accept, endpoint.contentType.toString())
      endpoint.body?.let { setBody(json.stringify(it)) }
    }
  }

  private fun createAuthHeader(): HttpAuthHeader? {
    val jwt = JWT.create().withJwt(
      jwt = Jwt(
        org = null,
        roles = setOf(JwtRole.SUPERUSER),
        user = JwtUser(UUID.randomUUID(), null, null)
      )
    ).sign(Algorithm.none())
    return HttpAuthHeader.Single("Bearer", jwt)
  }

  private fun JWTCreator.Builder.withJwt(jwt: Jwt): JWTCreator.Builder {
    withClaim(Claims.org, jwt.org?.let { json.stringify(it) })
    withClaim(Claims.roles, json.stringifySet(jwt.roles))
    withClaim(Claims.user, jwt.user?.let { json.stringify(it) })
    return this
  }

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
