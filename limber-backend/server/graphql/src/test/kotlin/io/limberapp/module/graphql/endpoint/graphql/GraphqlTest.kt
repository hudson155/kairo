package io.limberapp.module.graphql.endpoint.graphql

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.module.graphql.api.graphql.GraphqlApi
import io.limberapp.module.graphql.testing.IntegrationTest
import org.junit.jupiter.api.Test

internal class GraphqlTest(engine: TestApplicationEngine) : IntegrationTest(engine) {
  @Test
  fun graphql() {
    test(GraphqlApi) {}
  }
}
