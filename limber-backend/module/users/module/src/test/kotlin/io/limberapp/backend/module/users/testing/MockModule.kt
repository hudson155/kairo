package io.limberapp.backend.module.users.testing

import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.common.module.GuiceModule
import io.mockk.mockk

internal class MockModule : GuiceModule() {
  override fun configure() {
    bind(OrgService::class.java).toInstance(mockk())
  }

  override fun unconfigure() = Unit
}
