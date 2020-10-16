package io.limberapp.backend.module.auth.testing

import io.limberapp.backend.module.orgs.client.org.OrgClient
import io.limberapp.backend.module.users.client.account.UserClient
import io.limberapp.common.module.GuiceModule
import io.mockk.mockk

internal class MockModule : GuiceModule() {
  override fun configure() {
    bind(OrgClient::class.java).toInstance(mockk())
    bind(UserClient::class.java).toInstance(mockk())
  }

  override fun unconfigure() = Unit
}
