package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.store.isForeignKeyViolation
import com.piperframework.store.isUniqueConstraintViolation
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.exception.org.OrgRoleIsNotUnique
import io.limberapp.backend.module.orgs.exception.org.OrgRoleNotFound
import io.limberapp.backend.module.orgs.model.org.OrgRoleModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.UUID

private const val ORG_GUID_FOREIGN_KEY = "org_role_org_guid_fkey"
private const val ORG_ROLE_NAME_UNIQUE_CONSTRAINT = "org_role_org_guid_lower_idx"

internal class OrgRoleStore @Inject constructor(private val jdbi: Jdbi) : SqlStore() {
    fun create(model: OrgRoleModel) {
        jdbi.useHandle<Exception> {
            try {
                it.createUpdate(sqlResource("create"))
                    .bindKotlin(model)
                    .execute()
            } catch (e: UnableToExecuteStatementException) {
                handleCreateError(e)
            }
        }
    }

    private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
        val error = e.serverErrorMessage ?: throw e
        when {
            error.isForeignKeyViolation(ORG_GUID_FOREIGN_KEY) -> throw OrgNotFound()
            error.isUniqueConstraintViolation(ORG_ROLE_NAME_UNIQUE_CONSTRAINT) -> throw OrgRoleIsNotUnique()
            else -> throw e
        }
    }

    fun get(orgGuid: UUID, orgRoleGuid: UUID): OrgRoleModel? {
        return jdbi.withHandle<OrgRoleModel?, Exception> {
            it.createQuery(
                    """
                    SELECT *
                    FROM orgs.org_role
                    WHERE org_guid = :orgGuid
                      AND guid = :guid
                      AND archived_date IS NULL
                    """.trimIndent()
                )
                .bind("orgGuid", orgGuid)
                .bind("guid", orgRoleGuid)
                .mapTo(OrgRoleModel::class.java)
                .singleNullOrThrow()
        }
    }

    fun getByOrgGuid(orgGuid: UUID): Set<OrgRoleModel> {
        return jdbi.withHandle<Set<OrgRoleModel>, Exception> {
            it.createQuery("SELECT * FROM orgs.org_role WHERE org_guid = :orgGuid AND archived_date IS NULL")
                .bind("orgGuid", orgGuid)
                .mapTo(OrgRoleModel::class.java)
                .toSet()
        }
    }

    fun update(orgGuid: UUID, orgRoleGuid: UUID, update: OrgRoleModel.Update): OrgRoleModel {
        return jdbi.inTransaction<OrgRoleModel, Exception> {
            val updateCount = try {
                it.createUpdate(sqlResource("update"))
                    .bind("orgGuid", orgGuid)
                    .bind("guid", orgRoleGuid)
                    .bindKotlin(update)
                    .execute()
            } catch (e: UnableToExecuteStatementException) {
                handleUpdateError(e)
            }
            return@inTransaction when (updateCount) {
                0 -> throw OrgRoleNotFound()
                1 -> checkNotNull(get(orgGuid, orgRoleGuid))
                else -> badSql()
            }
        }
    }

    private fun handleUpdateError(e: UnableToExecuteStatementException): Nothing {
        val error = e.serverErrorMessage ?: throw e
        if (error.isUniqueConstraintViolation(ORG_ROLE_NAME_UNIQUE_CONSTRAINT)) throw OrgRoleIsNotUnique()
        else throw e
    }

    fun delete(orgGuid: UUID, orgRoleGuid: UUID) {
        jdbi.useTransaction<Exception> {
            val updateCount = it.createUpdate(
                    """
                    UPDATE orgs.org_role
                    SET archived_date = NOW()
                    WHERE org_guid = :orgGuid
                      AND guid = :guid
                      AND archived_date IS NULL
                    """.trimIndent()
                )
                .bind("orgGuid", orgGuid)
                .bind("guid", orgRoleGuid)
                .execute()
            return@useTransaction when (updateCount) {
                0 -> throw OrgRoleNotFound()
                1 -> Unit
                else -> badSql()
            }
        }
    }
}
