package io.limberapp.backend.module.users.store.account

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.users.model.account.AccountModel
import org.jdbi.v3.core.Jdbi
import java.util.*

internal class AccountStore @Inject constructor(private val jdbi: Jdbi) : SqlStore() {
  fun get(accountGuid: UUID): AccountModel? {
    return jdbi.withHandle<AccountModel?, Exception> {
      it.createQuery("SELECT * FROM users.account WHERE guid = :guid AND archived_date IS NULL")
        .bind("guid", accountGuid)
        .mapTo(AccountModel::class.java)
        .singleNullOrThrow()
    }
  }
}
