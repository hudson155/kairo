package io.limberapp.backend.api.test

import io.ktor.http.HttpMethod
import io.limberapp.common.restInterface.Endpoint
import io.limberapp.common.util.url.enc

internal object TestApi {
  internal object Singleton : Endpoint(HttpMethod.Get, "/singleton")

  internal data class Parameterized(
      val first: String,
      val second: String,
  ) : Endpoint(HttpMethod.Get, "/firsts/$first/seconds/$second/thirds")

  internal data class RequiredQueryParamFoo(
      val foo: String,
  ) : Endpoint(
      httpMethod = HttpMethod.Get,
      path = "/reqqp",
      queryParams = listOf("foo" to enc(foo)),
  )

  internal data class RequiredQueryParamBar(
      val bar: String,
  ) : Endpoint(
      httpMethod = HttpMethod.Get,
      path = "/reqqp",
      queryParams = listOf("bar" to enc(bar)),
  )

  internal data class OptionalQueryParam(
      val optional: String?,
  ) : Endpoint(
      httpMethod = HttpMethod.Get,
      path = "/optqp",
      queryParams = listOfNotNull(optional?.let { "optional" to enc(it) }),
  )
}
