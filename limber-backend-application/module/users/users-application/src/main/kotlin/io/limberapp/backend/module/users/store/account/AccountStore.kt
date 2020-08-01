package io.limberapp.backend.module.users.store.account

import com.google.inject.Inject
import com.google.inject.Singleton
import com.piperframework.store.SqlStore
import io.limberapp.backend.module.users.model.account.AccountModel
import org.jdbi.v3.core.Jdbi
import java.util.*

@Singleton
internal class AccountStore @Inject constructor(jdbi: Jdbi) : SqlStore(jdbi) {
  fun get(accountGuid: UUID): AccountModel? =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/account/get"))
        .bind("accountGuid", accountGuid)
        .mapTo(AccountModel::class.java)
        .findOne().orElse(null)
    }
}
