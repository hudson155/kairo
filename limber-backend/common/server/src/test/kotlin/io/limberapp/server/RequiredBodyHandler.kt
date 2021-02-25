package io.limberapp.server

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.auth.Auth
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template

internal class RequiredBodyHandler @Inject constructor(
) : EndpointHandler<TestApi.RequiredBody, TestRep.Complete>(
    template = TestApi.RequiredBody::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): TestApi.RequiredBody =
      TestApi.RequiredBody(rep = call.getAndValidateBody())

  override suspend fun Handler.handle(endpoint: TestApi.RequiredBody): TestRep.Complete {
    val rep = endpoint.rep.required()
    auth(Auth.Allow)
    return TestRep.Complete(foo = rep.foo)
  }
}
