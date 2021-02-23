package io.limberapp.api.test

import io.ktor.http.HttpMethod
import io.limberapp.restInterface.Endpoint

internal object TestApi {
  internal object Singleton : Endpoint(HttpMethod.Get, "/singleton")

  internal data class Parameterized(
      val first: String,
      val second: String,
  ) : Endpoint(HttpMethod.Get, "/firsts/$first/seconds/$second/thirds")

  internal data class RequiredQueryParamFoo(
      val foo: String,
  ) : Endpoint(HttpMethod.Get, "/reqqp", qp = listOf("foo" to foo))

  internal data class RequiredQueryParamBar(
      val bar: String,
  ) : Endpoint(HttpMethod.Get, "/reqqp", qp = listOf("bar" to bar))

  internal data class OptionalQueryParam(
      val optional: String?,
  ) : Endpoint(HttpMethod.Get, "/optqp", qp = listOfNotNull(optional?.let { "optional" to it }))
}
