package io.limberapp.common.restInterface

import io.ktor.http.HttpMethod
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class EndpointTest {
  @Test
  fun simple() {
    val endpoint = Endpoint(HttpMethod.Get, "/foo/bar")
    assertEquals("/foo/bar", endpoint.path)
    assertEquals("/foo/bar", endpoint.href)
  }

  @Test
  fun withPathParams() {
    val endpoint = Endpoint(
        httpMethod = HttpMethod.Get,
        rawPath = "/foo/bar/baz qux",
    )
    assertEquals("/foo/bar/baz%20qux", endpoint.path)
    assertEquals("/foo/bar/baz%20qux", endpoint.href)
  }

  @Test
  fun withQueryParams() {
    val endpoint = Endpoint(
        httpMethod = HttpMethod.Get,
        rawPath = "/foo/bar",
        qp = listOf("foo" to "foo value", "!@#$%" to "^&*()"),
    )
    assertEquals("/foo/bar", endpoint.path)
    assertEquals("/foo/bar?foo=foo+value&%21%40%23%24%25=%5E%26%2A%28%29", endpoint.href)
  }
}
