package io.limberapp.server

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.auth.Auth
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template

internal class OptionalBodyHandler @Inject constructor(
) : EndpointHandler<TestApi.OptionalBody, TestRep.Complete>(
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
