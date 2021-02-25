package io.limberapp.server

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.auth.Auth
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template

internal class RequiredQpHandler @Inject constructor(
) : EndpointHandler<TestApi.RequiredQp, TestRep.Complete>(
    template = TestApi.RequiredQp::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): TestApi.RequiredQp =
      TestApi.RequiredQp(foo = call.getParam(String::class, "foo"))

  override suspend fun Handler.handle(endpoint: TestApi.RequiredQp): TestRep.Complete {
    auth(Auth.Allow)
    // checkNotNull should not normally be required, but "foo" is nullable for test purposes.
    return TestRep.Complete(foo = checkNotNull(endpoint.foo))
  }
}
