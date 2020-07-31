package io.limberapp.backend.module.users.store.account

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import io.limberapp.backend.module.users.model.account.AccountModel
import org.jdbi.v3.core.Jdbi
import java.util.*

internal class AccountStore @Inject constructor(private val jdbi: Jdbi) : SqlStore(jdbi) {
  fun get(accountGuid: UUID? = null): List<AccountModel> {
    return jdbi.withHandle<List<AccountModel>, Exception> {
      it.createQuery("SELECT * FROM users.account WHERE guid = :guid AND archived_date IS NULL").build {
        if (accountGuid != null) {
          conditions.add("guid = :accountGuid")
          bindings["accountGuid"] = accountGuid
        }
      }
        .mapTo(AccountModel::class.java)
        .list()
    }
  }
}
