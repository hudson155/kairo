package io.limberapp.module.clients

import com.google.inject.Inject
import com.google.inject.Key
import com.google.inject.Provider
import com.google.inject.name.Names
import io.limberapp.client.HttpClient
import io.limberapp.client.SelfAuthenticatingHttpClient
import io.limberapp.config.AuthenticationMechanism
import io.limberapp.config.Hosts
import io.limberapp.module.Module
import io.limberapp.module.orgs.ORGS_FEATURE
import io.limberapp.serialization.LimberObjectMapper

internal class ClientsModule(
    private val hosts: Hosts,
    private val internalMechanism: AuthenticationMechanism.Jwt,
) : Module() {
  override fun bind() {
    bind(Hosts::class.java).toInstance(hosts)
    bind(AuthenticationMechanism.Jwt::class.java).toInstance(internalMechanism)

    with(Key.get(HttpClient::class.java, Names.named(ORGS_FEATURE))) {
      bind(this).toProvider(MonolithHttpClient::class.java).asEagerSingleton()
      expose(this)
    }
  }

  private class MonolithHttpClient @Inject constructor(
      private val hosts: Hosts,
      private val objectMapper: LimberObjectMapper,
      private val internalMechanism: AuthenticationMechanism.Jwt,
  ) : Provider<HttpClient> {
    override fun get(): HttpClient = SelfAuthenticatingHttpClient(
        baseUrl = hosts.monolith,
        objectMapper = objectMapper,
        algorithm = internalMechanism.createAlgorithm(),
        issuer = internalMechanism.issuer,
    )
  }

  override fun cleanUp(): Unit = Unit
}
