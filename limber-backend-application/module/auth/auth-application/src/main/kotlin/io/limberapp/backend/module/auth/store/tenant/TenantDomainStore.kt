package io.limberapp.backend.module.auth.store.tenant

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.store.isForeignKeyViolation
import com.piperframework.store.isUniqueConstraintViolation
import io.limberapp.backend.module.auth.exception.tenant.TenantDomainAlreadyRegistered
import io.limberapp.backend.module.auth.exception.tenant.TenantDomainNotFound
import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.model.tenant.TenantDomainModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.UUID

private const val ORG_GUID_FOREIGN_KEY = "tenant_domain_org_guid_fkey"
private const val DOMAIN_UNIQUE_CONSTRAINT = "tenant_domain_lower_idx"

internal class TenantDomainStore @Inject constructor(private val jdbi: Jdbi) : SqlStore() {
    fun create(orgGuid: UUID, models: Set<TenantDomainModel>) {
        jdbi.useTransaction<Exception> {
            try {
                it.prepareBatch(sqlResource("create")).apply {
                    models.forEach { bind("orgGuid", orgGuid).bindKotlin(it).add() }
                }.execute()
            } catch (e: UnableToExecuteStatementException) {
                handleCreateError(e)
            }
        }
    }

    fun create(orgGuid: UUID, model: TenantDomainModel) {
        jdbi.useTransaction<Exception> {
            try {
                it.createUpdate(sqlResource("create")).bind("orgGuid", orgGuid).bindKotlin(model).execute()
            } catch (e: UnableToExecuteStatementException) {
                handleCreateError(e)
            }
        }
    }

    private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
        val error = e.serverErrorMessage ?: throw e
        when {
            error.isForeignKeyViolation(ORG_GUID_FOREIGN_KEY) -> throw TenantNotFound()
            error.isUniqueConstraintViolation(DOMAIN_UNIQUE_CONSTRAINT) -> throw TenantDomainAlreadyRegistered()
            else -> throw e
        }
    }

    fun getByOrgGuid(orgGuid: UUID): Set<TenantDomainModel> {
        return jdbi.withHandle<Set<TenantDomainModel>, Exception> {
            it.createQuery("SELECT * FROM auth.tenant_domain WHERE org_guid = :orgGuid")
                .bind("orgGuid", orgGuid)
                .mapTo(TenantDomainModel::class.java)
                .toSet()
        }
    }

    fun delete(orgGuid: UUID, domain: String) {
        return jdbi.useTransaction<Exception> {
            val updateCount =
                it.createUpdate("DELETE FROM auth.tenant_domain WHERE org_guid = :orgGuid AND domain = :domain")
                    .bind("orgGuid", orgGuid)
                    .bind("domain", domain)
                    .execute()
            return@useTransaction when (updateCount) {
                0 -> throw TenantDomainNotFound()
                1 -> Unit
                else -> badSql()
            }
        }
    }
}
