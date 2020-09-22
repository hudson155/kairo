package io.limberapp.module.graphql.testing

import io.ktor.application.Application
import io.limberapp.graphqlServer.LimberGraphqlServer
import io.limberapp.testing.integration.IntegrationTestExtension

internal class IntegrationTestExtension : IntegrationTestExtension() {
  override fun Application.main() {
    LimberGraphqlServer(this, "test")
  }
}
