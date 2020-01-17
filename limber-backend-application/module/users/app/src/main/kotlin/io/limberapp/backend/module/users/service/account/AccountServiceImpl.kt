package io.limberapp.backend.module.users.service.account

import com.google.inject.Inject
import io.limberapp.backend.module.users.store.account.AccountStore

internal class AccountServiceImpl @Inject constructor(
    private val accountStore: AccountStore
) : AccountService by accountStore
