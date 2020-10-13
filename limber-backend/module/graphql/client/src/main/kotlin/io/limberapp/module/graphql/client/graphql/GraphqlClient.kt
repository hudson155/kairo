package io.limberapp.module.graphql.client.graphql

import io.limberapp.client.LimberHttpClient
import io.limberapp.client.LimberHttpClientRequestBuilder
import io.limberapp.module.graphql.api.graphql.GraphqlApi

class GraphqlClient(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
      endpoint: GraphqlApi,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    parse<Unit>(checkNotNull(it))
  }
}
