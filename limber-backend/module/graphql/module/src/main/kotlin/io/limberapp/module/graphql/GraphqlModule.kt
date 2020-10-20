package io.limberapp.module.graphql

import io.limberapp.common.endpoint.ApiEndpoint
import io.limberapp.common.module.ApplicationModule
import io.limberapp.module.graphql.endpoint.graphql.GraphqlEndpoint

class GraphqlModule : ApplicationModule() {
  override val endpoints = listOf<Class<out ApiEndpoint<*, *>>>(GraphqlEndpoint::class.java)

  override fun bindServices() = Unit
}
