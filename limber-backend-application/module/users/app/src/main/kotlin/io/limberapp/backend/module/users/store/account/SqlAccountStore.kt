package io.limberapp.backend.module.users.store.account

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.users.entity.user.AccountTable
import io.limberapp.backend.module.users.model.account.AccountModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import java.util.UUID

class SqlAccountStore @Inject constructor(
    database: Database
) : AccountStore, SqlStore(database) {

    override fun get(accountId: UUID) = transaction {
        return@transaction AccountTable
            .select { AccountTable.guid eq accountId }
            .singleOrNull()
            ?.toAccountModel()
    }

    private fun ResultRow.toAccountModel() = AccountModel(
        id = this[AccountTable.guid],
        created = this[AccountTable.createdDate],
        name = this[AccountTable.name],
        roles = mutableSetOf<JwtRole>().apply {
            if (this@toAccountModel[AccountTable.identityProvider]) add(JwtRole.IDENTITY_PROVIDER)
            if (this@toAccountModel[AccountTable.superuser]) add(JwtRole.SUPERUSER)
        }
    )
}
