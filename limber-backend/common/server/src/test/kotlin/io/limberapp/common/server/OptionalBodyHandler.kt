package io.limberapp.common.server

import io.ktor.application.ApplicationCall
import io.limberapp.common.auth.Auth
import io.limberapp.common.restInterface.EndpointHandler
import io.limberapp.common.restInterface.template

internal class OptionalBodyHandler : EndpointHandler<TestApi.OptionalBody, TestRep.Complete>(
    template = TestApi.OptionalBody::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): TestApi.OptionalBody =
      TestApi.OptionalBody(rep = call.getAndValidateBody())

  override suspend fun Handler.handle(endpoint: TestApi.OptionalBody): TestRep.Complete {
    val rep = endpoint.rep
    auth { Auth.Allow }
    return TestRep.Complete(foo = rep?.foo ?: "body missing")
  }
}
