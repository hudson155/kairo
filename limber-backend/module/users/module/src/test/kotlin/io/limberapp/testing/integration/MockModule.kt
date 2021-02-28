package io.limberapp.testing.integration

import io.limberapp.client.org.OrgClient

internal object MockModule : AbstractMockModule() {
  override fun bind() {
    mock(OrgClient::class)
  }
}
