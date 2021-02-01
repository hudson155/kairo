package io.limberapp.common.server

import io.ktor.application.ApplicationCall
import io.limberapp.common.auth.Auth
import io.limberapp.common.restInterface.EndpointHandler
import io.limberapp.common.restInterface.template

internal class OptionalQpHandler : EndpointHandler<TestApi.OptionalQp, TestRep.Complete>(
    template = TestApi.OptionalQp::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): TestApi.OptionalQp =
      TestApi.OptionalQp(foo = call.getParam(String::class, "foo", optional = true))

  override suspend fun Handler.handle(endpoint: TestApi.OptionalQp): TestRep.Complete {
    auth { Auth.Allow }
    return TestRep.Complete(foo = endpoint.foo ?: "qp missing")
  }
}
