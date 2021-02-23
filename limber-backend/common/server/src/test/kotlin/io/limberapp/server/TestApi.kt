package io.limberapp.server

import io.ktor.http.HttpMethod
import io.ktor.http.encodeURLPath
import io.limberapp.restInterface.Endpoint

internal object TestApi {
  internal object NoopGet : Endpoint(HttpMethod.Get, "/noop-get")

  internal object EndpointWithoutAuth : Endpoint(HttpMethod.Get, "/endpoint-without-auth")

  internal object RequiresPermission : Endpoint(HttpMethod.Get, "/requires-permission")

  internal data class PathParam(
      val foo: String,
  ) : Endpoint(HttpMethod.Get, "/path/${foo.encodeURLPath()}/param")

  internal data class RequiredQp(
      val foo: String?, // Normally the param would be non-nullable for a required query param.
      // However, for testing we want the ability to incorrectly pass no value.
  ) : Endpoint(HttpMethod.Get, "/req-query-param", qp = listOfNotNull(foo?.let { "foo" to it }))

  internal data class OptionalQp(
      val foo: String?,
  ) : Endpoint(HttpMethod.Get, "/opt-query-param", qp = listOfNotNull(foo?.let { "foo" to it }))

  internal data class RequiredBody(
      val rep: TestRep.Creation?,
  ) : Endpoint(HttpMethod.Post, "/req-body", body = rep)

  internal data class OptionalBody(
      val rep: TestRep.Creation?,
  ) : Endpoint(HttpMethod.Post, "/opt-body", body = rep)

  internal object Missing : Endpoint(HttpMethod.Get, "/missing")
}
