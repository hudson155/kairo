package io.limberapp.server

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.auth.Auth
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template

internal class OptionalQpHandler @Inject constructor(
) : EndpointHandler<TestApi.OptionalQp, TestRep.Complete>(
    template = TestApi.OptionalQp::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): TestApi.OptionalQp =
      TestApi.OptionalQp(foo = call.getParam(String::class, "foo", optional = true))

  override suspend fun Handler.handle(endpoint: TestApi.OptionalQp): TestRep.Complete {
    auth { Auth.Allow }
    return TestRep.Complete(foo = endpoint.foo ?: "qp missing")
  }
}
