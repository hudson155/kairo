package io.limberapp.backend.module.orgs.testing

import io.limberapp.common.module.GuiceModule

internal class MockModule : GuiceModule() {
  override fun configure() = Unit

  override fun unconfigure() = Unit
}
