package io.limberapp.testing.integration

import io.limberapp.client.org.OrgClient
import io.limberapp.client.user.UserClient

internal object MockModule : AbstractMockModule() {
  override fun bind() {
    mock(UserClient::class)
    mock(OrgClient::class)
  }
}
