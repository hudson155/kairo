package io.limberapp.module.graphql.endpoint.graphql

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Auth
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.common.restInterface.template
import io.limberapp.module.graphql.api.graphql.GraphqlApi

internal class GraphqlEndpoint @Inject constructor(
    application: Application,
) : LimberApiEndpoint<GraphqlApi, Unit>(
    application = application,
    endpointTemplate = GraphqlApi::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = GraphqlApi

  override suspend fun Handler.handle(command: GraphqlApi) {
    auth { Auth.AnyJwt }
  }
}
