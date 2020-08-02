package io.limberapp.backend.module.users.store.account

import com.piperframework.store.QueryBuilder
import io.limberapp.backend.module.users.model.account.AccountFinder
import java.util.*

internal class AccountQueryBuilder : QueryBuilder(), AccountFinder {
  override fun accountGuid(accountGuid: UUID) {
    conditions += "guid = :accountGuid"
    bindings["accountGuid"] = accountGuid
  }
}
