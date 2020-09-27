package io.limberapp.backend.module.forms.testing

import io.limberapp.backend.module.orgs.service.org.FeatureService
import io.limberapp.backend.module.users.service.account.UserService
import io.limberapp.common.module.GuiceModule
import io.mockk.mockk

internal class MockModule : GuiceModule() {
  override fun configure() {
    bind(FeatureService::class.java).toInstance(mockk())
    bind(UserService::class.java).toInstance(mockk())
  }

  override fun unconfigure() = Unit
}
