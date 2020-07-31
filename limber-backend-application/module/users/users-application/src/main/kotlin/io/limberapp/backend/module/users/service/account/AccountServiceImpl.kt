package io.limberapp.backend.module.users.service.account

import com.google.inject.Inject
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.users.store.account.AccountStore
import java.util.*

internal class AccountServiceImpl @Inject constructor(
  private val accountStore: AccountStore
) : AccountService {
  override fun get(accountGuid: UUID) = accountStore.get(accountGuid = accountGuid).singleNullOrThrow()
}
