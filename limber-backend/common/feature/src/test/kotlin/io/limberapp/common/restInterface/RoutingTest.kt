package io.limberapp.common.restInterface

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import io.ktor.util.pipeline.ContextDsl
import io.limberapp.backend.api.test.TestApi
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class RoutingTest {
  @Test
  fun `singleton endpoint`() {
    withApplication {
      // Everything's correct.
      with(handleRequest(HttpMethod.Get, "/singleton")) {
        assertTrue(requestHandled)
        assertEquals(TestApi.Singleton::class.simpleName, response.content)
      }
      // Everything's correct, explicit Accept header.
      with(handleRequest(HttpMethod.Get, "/singleton") {
        addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
      }) {
        assertTrue(requestHandled)
        assertEquals(TestApi.Singleton::class.simpleName, response.content)
      }
      // Trailing slash is no good.
      with(handleRequest(HttpMethod.Get, "/singleton/")) {
        assertFalse(requestHandled)
      }
      // Method doesn't match.
      with(handleRequest(HttpMethod.Post, "/singleton")) {
        assertFalse(requestHandled)
      }
      // Accept doesn't match.
      with(handleRequest(HttpMethod.Get, "/singleton") {
        addHeader(HttpHeaders.Accept, ContentType.Text.CSV.toString())
      }) {
        assertFalse(requestHandled)
      }
    }
  }

  @Test
  fun `parameterized endpoint`() {
    withApplication {
      // Path is incomplete.
      with(handleRequest(HttpMethod.Get, "/firsts/1/seconds/2")) {
        assertFalse(requestHandled)
      }
      // Everything's correct.
      with(handleRequest(HttpMethod.Get, "/firsts/1/seconds/2/thirds")) {
        assertTrue(requestHandled)
        assertEquals(TestApi.Parameterized::class.simpleName, response.content)
      }
      // Parameter is missing.
      with(handleRequest(HttpMethod.Get, "/firsts//seconds/2/thirds")) {
        assertFalse(requestHandled)
      }
    }
  }

  @Test
  fun `required query param`() {
    withApplication {
      // Query param is missing.
      with(handleRequest(HttpMethod.Get, "/reqqp")) {
        assertFalse(requestHandled)
      }
      // Query param matches Foo endpoint.
      with(handleRequest(HttpMethod.Get, "/reqqp?foo=foo+value")) {
        assertTrue(requestHandled)
        assertEquals(TestApi.RequiredQueryParamFoo::class.simpleName, response.content)
      }
      // Query param matches Bar endpoint.
      with(handleRequest(HttpMethod.Get, "/reqqp?bar=bar+value")) {
        assertTrue(requestHandled)
        assertEquals(TestApi.RequiredQueryParamBar::class.simpleName, response.content)
      }
    }
  }

  @Test
  fun `optional query param`() {
    // Query param is missing.
    withApplication {
      with(handleRequest(HttpMethod.Get, "/optqp")) {
        assertTrue(requestHandled)
        assertEquals(TestApi.OptionalQueryParam::class.simpleName, response.content)
      }
      // Query param is present, in addition to an extra query param.
      with(handleRequest(HttpMethod.Get, "/optqp?foo=foo+value&bar=bar+value&optional=123")) {
        assertTrue(requestHandled)
        assertEquals(TestApi.OptionalQueryParam::class.simpleName, response.content)
      }
    }
  }

  private fun withApplication(function: TestApplicationEngine.() -> Unit) {
    withTestApplication(
        moduleFunction = {
          install(Routing) {
            route(TestApi.Singleton::class)
            route(TestApi.Parameterized::class)
            route(TestApi.RequiredQueryParamFoo::class)
            route(TestApi.RequiredQueryParamBar::class)
            route(TestApi.OptionalQueryParam::class)
          }
        },
        test = function,
    )
  }

  @ContextDsl
  private fun Routing.route(endpoint: KClass<out Endpoint>) {
    route(endpoint.template()) {
      // Always respond with the endpoint name.
      call.respondText(checkNotNull(endpoint.simpleName))
    }
  }
}
