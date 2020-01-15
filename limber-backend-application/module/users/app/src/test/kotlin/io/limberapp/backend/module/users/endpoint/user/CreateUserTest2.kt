//package io.limberapp.backend.module.users.endpoint.user
//
//import com.auth0.jwt.JWT
//import com.auth0.jwt.JWTCreator
//import com.auth0.jwt.algorithms.Algorithm
//import com.fasterxml.jackson.module.kotlin.readValue
//import com.google.inject.Injector
//import com.piperframework.SimplePiperApp
//import com.piperframework.config.Config
//import com.piperframework.config.authentication.AuthenticationConfig
//import com.piperframework.config.authentication.UnsignedJwtAuthentication
//import com.piperframework.config.serving.ServingConfig
//import com.piperframework.config.serving.StaticFiles
//import com.piperframework.endpoint.EndpointConfig
//import com.piperframework.error.PiperError
//import com.piperframework.exception.PiperException
//import com.piperframework.exceptionMapping.ExceptionMapper
//import com.piperframework.jackson.objectMapper.PiperObjectMapper
//import com.piperframework.ktorAuth.piperAuth
//import com.piperframework.module.MainModule
//import com.piperframework.module.TestSqlModule
//import com.piperframework.util.uuid.uuidGenerator.DeterministicUuidGenerator
//import io.ktor.application.Application
//import io.ktor.application.ApplicationStarted
//import io.ktor.auth.Authentication
//import io.ktor.http.ContentType
//import io.ktor.http.HttpHeaders
//import io.ktor.http.HttpStatusCode
//import io.ktor.http.auth.HttpAuthHeader
//import io.ktor.server.testing.TestApplicationCall
//import io.ktor.server.testing.TestApplicationEngine
//import io.ktor.server.testing.createTestEnvironment
//import io.ktor.server.testing.handleRequest
//import io.ktor.server.testing.setBody
//import io.limberapp.backend.authentication.jwt.JwtAuthVerifier
//import io.limberapp.backend.authorization.principal.Claims
//import io.limberapp.backend.authorization.principal.Jwt
//import io.limberapp.backend.authorization.principal.JwtRole
//import io.limberapp.backend.authorization.principal.JwtUser
//import io.limberapp.backend.module.users.UsersModule
//import io.limberapp.backend.module.users.exception.conflict.EmailAddressAlreadyTaken
//import io.limberapp.backend.module.users.testing.fixtures.user.UserRepFixtures
//import org.spekframework.spek2.Spek
//import org.spekframework.spek2.dsl.Root
//import org.spekframework.spek2.lifecycle.LifecycleListener
//import java.time.Clock
//import java.time.Instant
//import java.time.ZoneId
//import java.util.UUID
//import kotlin.test.assertEquals
//import kotlin.test.assertTrue
//
//abstract class PiperSpek(root: Root.() -> Unit) : Spek(root)
//
//class MyTest : PiperSpek({
//
//    registerListener(object : LifecycleListener {
//
//    })
//
//    val objectMapper by memoized { PiperObjectMapper() }
//
//    val exceptionMapper = ExceptionMapper()
//
//    val testSqlModule = TestSqlModule()
//
//    val engine = TestApplicationEngine(createTestEnvironment())
//    engine.start()
//
//    val application = engine.application
//
//    val config = object : Config {
//        override val serving = ServingConfig(
//            apiPathPrefix = "/",
//            staticFiles = StaticFiles(false)
//        )
//        override val authentication =
//            AuthenticationConfig(listOf(UnsignedJwtAuthentication))
//    }
//
//    val fixedClock: Clock = Clock.fixed(Instant.parse("2007-12-03T10:15:30.00Z"), ZoneId.of("America/New_York"))
//
//    val deterministicUuidGenerator = DeterministicUuidGenerator()
//
//    val testApp = object : SimplePiperApp<Config>(application, config) {
//
//        override fun Authentication.Configuration.configureAuthentication(injector: Injector) {
//            piperAuth<Jwt> {
//                verifier(JwtAuthVerifier.scheme, JwtAuthVerifier(config.authentication), default = true)
//            }
//        }
//
//        override fun getMainModules(application: Application) =
//            listOf(MainModule(application, fixedClock, config, deterministicUuidGenerator)).plus(listOf(testSqlModule))
//
//        override val modules = listOf(UsersModule())
//    }
//
//    try {
//        // TestApplicationEngine does not raise ApplicationStarted.
//        engine.environment.monitor.raise(ApplicationStarted, engine.application)
//    } catch (e: Throwable) {
//        // TODO: stop the engine
//        throw e
//    }
//
//    fun JWTCreator.Builder.withJwt(jwt: Jwt): JWTCreator.Builder {
//        withClaim(Claims.org, objectMapper.writeValueAsString(jwt.org))
//        withClaim(Claims.roles, objectMapper.writeValueAsString(jwt.roles))
//        withClaim(Claims.user, objectMapper.writeValueAsString(jwt.user))
//        return this
//    }
//
//    fun createAuthHeader(): HttpAuthHeader? {
//        val jwt = JWT.create().withJwt(
//            jwt = Jwt(
//                org = null,
//                roles = setOf(JwtRole.SUPERUSER),
//                user = JwtUser(UUID.randomUUID(), "Jeff", "Hudson")
//            )
//        ).sign(Algorithm.none())
//        return HttpAuthHeader.Single("Bearer", jwt)
//    }
//
//    fun TestApplicationEngine.createCall(
//        endpointConfig: EndpointConfig,
//        pathParams: Map<String, String>,
//        queryParams: Map<String, String>,
//        body: Any?
//    ): TestApplicationCall {
//        return handleRequest(endpointConfig.httpMethod, endpointConfig.path(pathParams, queryParams)) {
//            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
//            createAuthHeader()?.let { addHeader(HttpHeaders.Authorization, it.toString()) }
//            body?.let { setBody(objectMapper.writeValueAsString(it)) }
//        }
//    }
//
//    fun TestApplicationCall.runTest(
//        expectedStatusCode: HttpStatusCode,
//        test: TestApplicationCall.() -> Unit
//    ) {
//        assertTrue(
//            actual = requestHandled,
//            message = "The HTTP request was not handled." +
//                    " Is the path wrong or did you forget to register the ApiEndpoint?"
//        )
//        assertEquals(
//            expectedStatusCode,
//            response.status(),
//            "Unexpected HTTP response code.\nResponse: ${response.content}\n"
//        )
//        test()
//    }
//
//    fun request(
//        endpointConfig: EndpointConfig,
//        pathParams: Map<String, Any>,
//        queryParams: Map<String, Any>,
//        body: Any?,
//        expectedStatusCode: HttpStatusCode,
//        test: TestApplicationCall.() -> Unit
//    ) {
//        engine.createCall(
//            endpointConfig = endpointConfig,
//            pathParams = pathParams.mapValues { it.value.toString() },
//            queryParams = queryParams.mapValues { it.value.toString() },
//            body = body
//        ).runTest(expectedStatusCode, test)
//    }
//
//    fun setup(
//        endpointConfig: EndpointConfig,
//        pathParams: Map<String, Any> = emptyMap(),
//        queryParams: Map<String, Any> = emptyMap(),
//        body: Any? = null
//    ) = request(
//        endpointConfig = endpointConfig,
//        pathParams = pathParams,
//        queryParams = queryParams,
//        body = body,
//        expectedStatusCode = HttpStatusCode.OK,
//        test = {}
//    )
//
//    fun test(
//        endpointConfig: EndpointConfig,
//        pathParams: Map<String, Any> = emptyMap(),
//        queryParams: Map<String, Any> = emptyMap(),
//        body: Any? = null,
//        expectedStatusCode: HttpStatusCode = HttpStatusCode.OK,
//        test: TestApplicationCall.() -> Unit
//    ) = request(
//        endpointConfig = endpointConfig,
//        pathParams = pathParams,
//        queryParams = queryParams,
//        body = body,
//        expectedStatusCode = expectedStatusCode,
//        test = test
//    )
//
//    fun test(
//        endpointConfig: EndpointConfig,
//        pathParams: Map<String, Any> = emptyMap(),
//        queryParams: Map<String, Any> = emptyMap(),
//        body: Any? = null,
//        expectedException: PiperException
//    ) {
//        val expectedError = exceptionMapper.handle(expectedException)
//        request(
//            endpointConfig = endpointConfig,
//            pathParams = pathParams,
//            queryParams = queryParams,
//            body = body,
//            expectedStatusCode = HttpStatusCode.fromValue(expectedError.statusCode),
//            test = {
//                val actual = objectMapper.readValue<PiperError>(response.content!!)
//                assertEquals(expectedError, actual)
//            }
//        )
//    }
//
//    test("duplicate email address") {
//
//        // CreateUser
//        setup(
//            endpointConfig = CreateUser.endpointConfig,
//            body = UserRepFixtures[0].creation()
//        )
//
//        // CreateUser
//        test(
//            endpointConfig = CreateUser.endpointConfig,
//            body = UserRepFixtures[1].creation().copy(emailAddress = UserRepFixtures[0].creation().emailAddress),
//            expectedException = EmailAddressAlreadyTaken(UserRepFixtures[0].creation().emailAddress)
//        )
//    }
//})
