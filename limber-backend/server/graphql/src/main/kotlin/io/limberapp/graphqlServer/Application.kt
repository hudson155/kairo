package io.limberapp.graphqlServer

import io.ktor.application.Application

@Suppress("Unused")
internal fun Application.main() {
  LimberGraphqlServer(this)
}
