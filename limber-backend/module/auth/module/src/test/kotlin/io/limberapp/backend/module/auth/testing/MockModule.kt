package io.limberapp.backend.module.auth.testing

import io.limberapp.backend.module.orgs.service.org.FeatureService
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.users.service.account.UserService
import io.limberapp.common.module.GuiceModule
import io.mockk.mockk

internal class MockModule : GuiceModule() {
  override fun configure() {
    bind(FeatureService::class.java).toInstance(mockk())
    bind(OrgService::class.java).toInstance(mockk())
    bind(UserService::class.java).toInstance(mockk())
  }

  override fun unconfigure() = Unit
}
