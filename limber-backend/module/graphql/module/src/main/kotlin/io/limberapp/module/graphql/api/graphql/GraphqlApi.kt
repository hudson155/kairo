package io.limberapp.module.graphql.api.graphql

import io.ktor.http.HttpMethod
import io.limberapp.common.restInterface.LimberEndpoint

object GraphqlApi : LimberEndpoint(
    httpMethod = HttpMethod.Post,
    path = "/graphql",
)
