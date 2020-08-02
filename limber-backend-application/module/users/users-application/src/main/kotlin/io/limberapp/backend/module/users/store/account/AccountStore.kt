package io.limberapp.backend.module.users.store.account

import com.google.inject.Inject
import com.google.inject.Singleton
import com.piperframework.finder.Finder
import com.piperframework.store.SqlStore
import com.piperframework.store.withFinder
import io.limberapp.backend.module.users.model.account.AccountFinder
import io.limberapp.backend.module.users.model.account.AccountModel
import org.jdbi.v3.core.Jdbi

@Singleton
internal class AccountStore @Inject constructor(jdbi: Jdbi) : SqlStore(jdbi), Finder<AccountModel, AccountFinder> {
  override fun <R> find(result: (Iterable<AccountModel>) -> R, query: AccountFinder.() -> Unit): R =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/account/get"))
        .withFinder(AccountQueryBuilder().apply(query))
        .mapTo(AccountModel::class.java)
        .let(result)
    }
}
