package io.limberapp.backend.module.users.store.account

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.users.model.account.AccountModel
import org.jdbi.v3.core.Jdbi
import org.jetbrains.exposed.sql.Database
import java.util.UUID

internal class AccountStore @Inject constructor(
    database: Database,
    private val jdbi: Jdbi
) : SqlStore(database) {
    fun get(accountGuid: UUID) = jdbi.withHandle<AccountModel?, Exception> {
        it.createQuery("SELECT * FROM users.account WHERE guid = :guid")
            .bind("guid", accountGuid)
            .mapTo(AccountModel::class.java)
            .singleNullOrThrow()
    }
}
