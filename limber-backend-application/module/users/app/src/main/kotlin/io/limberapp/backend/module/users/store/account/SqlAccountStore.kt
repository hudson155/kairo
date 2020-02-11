package io.limberapp.backend.module.users.store.account

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import io.limberapp.backend.module.users.entity.account.AccountTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.select
import java.util.UUID

internal class SqlAccountStore @Inject constructor(
    database: Database,
    private val sqlAccountMapper: SqlAccountMapper
) : AccountStore, SqlStore(database) {

    override fun get(accountId: UUID) = transaction {
        val entity = AccountTable
            .select { AccountTable.guid eq accountId }
            .singleOrNull() ?: return@transaction null
        return@transaction sqlAccountMapper.accountModel(entity)
    }
}
