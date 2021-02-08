package io.limberapp.common.server

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.common.auth.Auth
import io.limberapp.common.restInterface.EndpointHandler
import io.limberapp.common.restInterface.template

internal class NoopGetHandler @Inject constructor(
) : EndpointHandler<TestApi.NoopGet, Unit>(
    template = TestApi.NoopGet::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): TestApi.NoopGet =
      TestApi.NoopGet

  override suspend fun Handler.handle(endpoint: TestApi.NoopGet) {
    auth { Auth.Allow }
  }
}
