package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.store.isForeignKeyViolation
import com.piperframework.store.isUniqueConstraintViolation
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.exception.org.OrgRoleIsNotUnique
import io.limberapp.backend.module.orgs.model.org.OrgRoleModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.UUID

private const val ORG_GUID_FOREIGN_KEY = "org_role_org_guid_fkey"
private const val ORG_ROLE_NAME_UNIQUE_CONSTRAINT = "org_role_org_guid_lower_idx"

internal class OrgRoleStore @Inject constructor(private val jdbi: Jdbi) : SqlStore() {
    fun create(orgGuid: UUID, model: OrgRoleModel) {
        jdbi.useHandle<Exception> {
            try {
                it.createUpdate(sqlResource("create"))
                    .bind("orgGuid", orgGuid)
                    .bindKotlin(model)
                    .execute()
            } catch (e: UnableToExecuteStatementException) {
                handleCreateError(e)
            }
        }
    }

    private fun handleCreateError(e: UnableToExecuteStatementException) {
        val error = e.serverErrorMessage ?: throw e
        when {
            error.isForeignKeyViolation(ORG_GUID_FOREIGN_KEY) -> throw OrgNotFound()
            error.isUniqueConstraintViolation(ORG_ROLE_NAME_UNIQUE_CONSTRAINT) -> throw OrgRoleIsNotUnique()
            else -> throw e
        }
    }
}
