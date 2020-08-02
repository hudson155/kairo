package io.limberapp.backend.module.users.service.account

import com.google.inject.Inject
import com.piperframework.finder.Finder
import io.limberapp.backend.module.users.model.account.AccountFinder
import io.limberapp.backend.module.users.model.account.AccountModel
import io.limberapp.backend.module.users.store.account.AccountStore

internal class AccountServiceImpl @Inject constructor(
  private val accountStore: AccountStore
) : AccountService, Finder<AccountModel, AccountFinder> by accountStore
