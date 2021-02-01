package io.limberapp.common.server

import io.ktor.application.ApplicationCall
import io.limberapp.common.auth.Auth
import io.limberapp.common.restInterface.EndpointHandler
import io.limberapp.common.restInterface.template

internal class PathParamHandler : EndpointHandler<TestApi.PathParam, TestRep.Complete>(
    template = TestApi.PathParam::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): TestApi.PathParam =
      TestApi.PathParam(foo = call.getParam(String::class, "foo"))

  override suspend fun Handler.handle(endpoint: TestApi.PathParam): TestRep.Complete {
    auth { Auth.Allow }
    return TestRep.Complete(foo = endpoint.foo)
  }
}
