package io.limberapp.backend.module.users.service.account

import com.piperframework.finder.Finder
import io.limberapp.backend.LimberModule
import io.limberapp.backend.module.users.model.account.AccountFinder
import io.limberapp.backend.module.users.model.account.AccountModel

@LimberModule.Users
interface AccountService : Finder<AccountModel, AccountFinder>
