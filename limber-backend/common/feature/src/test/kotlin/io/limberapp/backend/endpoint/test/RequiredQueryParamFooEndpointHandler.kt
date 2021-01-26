package io.limberapp.backend.endpoint.test

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.backend.api.test.TestApi
import io.limberapp.backend.service.test.TestService
import io.limberapp.common.restInterface.EndpointHandler
import io.limberapp.common.restInterface.template

internal class RequiredQueryParamFooEndpointHandler @Inject constructor(
    @Suppress("UNUSED_PARAMETER") testService: TestService,
) : EndpointHandler<TestApi.RequiredQueryParamFoo, Unit>(
    template = TestApi.RequiredQueryParamFoo::class.template(),
) {
  override suspend fun endpoint(
      call: ApplicationCall,
  ): TestApi.RequiredQueryParamFoo = TestApi.RequiredQueryParamFoo(
      foo = call.getParam(String::class, "foo"),
  )

  override suspend fun Handler.handle(endpoint: TestApi.RequiredQueryParamFoo): Unit = Unit
}
