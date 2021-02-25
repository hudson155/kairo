package io.limberapp.server

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.auth.Auth
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template

internal class PathParamHandler @Inject constructor(
) : EndpointHandler<TestApi.PathParam, TestRep.Complete>(
    template = TestApi.PathParam::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): TestApi.PathParam =
      TestApi.PathParam(foo = call.getParam(String::class, "foo"))

  override suspend fun Handler.handle(endpoint: TestApi.PathParam): TestRep.Complete {
    auth(Auth.Allow)
    return TestRep.Complete(foo = endpoint.foo)
  }
}
