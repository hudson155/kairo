package io.limberapp.common.server

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.Application
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.cio.CIO
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.limberapp.common.auth.jwt.JwtClaims
import io.limberapp.common.client.HttpClientImpl
import io.limberapp.common.client.RequestBuilder
import io.limberapp.common.client.exception.LimberHttpClientException
import io.limberapp.common.module.Module
import io.limberapp.common.permissions.limberPermissions.LimberPermission
import io.limberapp.common.permissions.limberPermissions.LimberPermissions
import io.limberapp.common.serialization.LimberObjectMapper
import io.limberapp.common.typeConversion.typeConverter.LimberPermissionsTypeConverter
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.net.ConnectException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ServerTest {
  internal class TestServer(application: Application) :
      Server<TestConfig>(application, TestConfig) {
    override val modules: List<Module> = listOf(TestFeature)
  }

  private val objectMapper: LimberObjectMapper =
      LimberObjectMapper(typeConverters = setOf(LimberPermissionsTypeConverter))

  private lateinit var engine: ApplicationEngine

  private val client: TestClient =
      TestClient(HttpClientImpl("http://localhost:55101", objectMapper))

  @BeforeAll
  fun beforeAll() {
    engine = embeddedServer(CIO, port = 55_101) {
      TestServer(this)
    }.start()
    runBlocking { pollUntilServerIsReady(10_000, 100) }
  }

  /**
   * Polls the server every [pollDelayMs] until it stops throwing [ConnectException]s, up to a
   * maximum of [maxPollMs]. If it's not ready by that point, throws an exception.
   */
  private suspend fun pollUntilServerIsReady(maxPollMs: Long, pollDelayMs: Long) {
    var pollMsRemaining = maxPollMs
    while (pollMsRemaining > 0) {
      delay(pollDelayMs)
      try {
        client(TestApi.NoopGet, authHeader(null))
        return
      } catch (_: ConnectException) {
        pollMsRemaining -= pollDelayMs
      }
    }
    error("Server is not ready")
  }

  @AfterAll
  fun afterAll() {
    engine.stop(1000, 5000)
  }

  @Test
  fun `Simple no-op endpoint`(): Unit = runBlocking {
    val result = client(TestApi.NoopGet, authHeader(null))
    assertEquals(Unit, result)
  }

  @Test
  fun `Endpoint without auth`(): Unit = runBlocking {
    assertFailsWith<LimberHttpClientException> {
      client(TestApi.EndpointWithoutAuth, authHeader(null))
    }.let { e ->
      assertEquals(HttpStatusCode.InternalServerError, e.statusCode)
    }
  }

  @Test
  fun `Endpoint requires permission, no JWT`(): Unit = runBlocking {
    assertFailsWith<LimberHttpClientException> {
      client(TestApi.RequiresPermission, authHeader(null))
    }.let { e ->
      assertEquals(HttpStatusCode.Unauthorized, e.statusCode)
    }
  }

  @Test
  fun `Endpoint requires permission, JWT without permission`(): Unit = runBlocking {
    assertFailsWith<LimberHttpClientException> {
      client(TestApi.RequiresPermission, authHeader(LimberPermissions.none()))
    }.let { e ->
      assertEquals(HttpStatusCode.Forbidden, e.statusCode)
    }
  }

  @Test
  fun `Endpoint requires permission, JWT has permission`(): Unit = runBlocking {
    val authHeader = authHeader(LimberPermissions(setOf(LimberPermission.SUPERUSER)))
    val result = client(TestApi.RequiresPermission, authHeader)
    assertEquals(Unit, result)
  }

  @Test
  fun `Path param`(): Unit = runBlocking {
    val result = client(TestApi.PathParam("foo value"), authHeader(null))
    assertEquals(TestRep.Complete("foo value"), result)
  }

  @Test
  fun `Required query param with QP missing`(): Unit = runBlocking {
    assertFailsWith<LimberHttpClientException> {
      client(TestApi.RequiredQp(null), authHeader(null))
    }.let { e ->
      assertEquals(HttpStatusCode.BadRequest, e.statusCode)
    }
  }

  @Test
  fun `Required query param with QP present`(): Unit = runBlocking {
    val result = client(TestApi.RequiredQp("foo value"), authHeader(null))
    assertEquals(TestRep.Complete("foo value"), result)
  }

  @Test
  fun `Optional query param with QP missing`(): Unit = runBlocking {
    val result = client(TestApi.OptionalQp(null), authHeader(null))
    assertEquals(TestRep.Complete("qp missing"), result)
  }

  @Test
  fun `Optional query param with QP present`(): Unit = runBlocking {
    val result = client(TestApi.OptionalQp("foo value"), authHeader(null))
    assertEquals(TestRep.Complete("foo value"), result)
  }

  @Test
  fun `Required body (POST) with body missing`(): Unit = runBlocking {
    assertFailsWith<LimberHttpClientException> {
      client(TestApi.RequiredBody(null), authHeader(null))
    }.let { e ->
      assertEquals(HttpStatusCode.BadRequest, e.statusCode)
    }
  }

  @Test
  fun `Required body (POST) with body present`(): Unit = runBlocking {
    val creationRep = TestRep.Creation("foo value")
    val result = client(TestApi.RequiredBody(creationRep), authHeader(null))
    assertEquals(TestRep.Complete("foo value"), result)
  }

  @Test
  fun `Optional body (POST) with body missing`(): Unit = runBlocking {
    val result = client(TestApi.OptionalBody(null), authHeader(null))
    assertEquals(TestRep.Complete("body missing"), result)
  }

  @Test
  fun `Optional body (POST) with body present`(): Unit = runBlocking {
    val creationRep = TestRep.Creation("foo value")
    val result = client(TestApi.OptionalBody(creationRep), authHeader(null))
    assertEquals(TestRep.Complete("foo value"), result)
  }

  @Test
  fun `Endpoint does not exist (is not bound)`(): Unit = runBlocking {
    val result = client(TestApi.Missing, authHeader(null))
    assertNull(result)
  }

  private fun authHeader(permissions: LimberPermissions?): RequestBuilder = {
    if (permissions != null) {
      val jwt = JWT.create()
          .withClaim(JwtClaims.permissions, permissions.asDarb())
          .sign(Algorithm.none())
      putHeader(HttpHeaders.Authorization, "Bearer $jwt")
    }
  }
}
