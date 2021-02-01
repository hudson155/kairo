package io.limberapp.common.server

import io.ktor.application.ApplicationCall
import io.limberapp.common.auth.Auth
import io.limberapp.common.restInterface.EndpointHandler
import io.limberapp.common.restInterface.template

internal class RequiredBodyHandler : EndpointHandler<TestApi.RequiredBody, TestRep.Complete>(
    template = TestApi.RequiredBody::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): TestApi.RequiredBody =
      TestApi.RequiredBody(rep = call.getAndValidateBody())

  override suspend fun Handler.handle(endpoint: TestApi.RequiredBody): TestRep.Complete {
    val rep = endpoint.rep.required()
    auth { Auth.Allow }
    return TestRep.Complete(foo = rep.foo)
  }
}
