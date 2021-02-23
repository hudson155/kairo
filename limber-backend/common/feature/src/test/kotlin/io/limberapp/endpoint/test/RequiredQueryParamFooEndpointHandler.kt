package io.limberapp.endpoint.test

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.test.TestApi
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.test.TestService

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
