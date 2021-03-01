package io.limberapp.store.tenant

import com.google.inject.Inject
import com.google.inject.Singleton
import io.limberapp.model.tenant.TenantDomainModel
import io.limberapp.exception.tenant.TenantDomainAlreadyRegistered
import io.limberapp.exception.tenant.TenantDomainNotFound
import io.limberapp.exception.tenant.TenantNotFound
import io.limberapp.exception.unprocessable
import io.limberapp.sql.store.SqlStore
import io.limberapp.sql.store.isForeignKeyViolation
import io.limberapp.sql.store.isUniqueConstraintViolation
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.UUID

private const val FK_ORG_GUID = "fk__tenant_domain__org_guid"
private const val UNIQ_DOMAIN = "uniq__tenant_domain__domain"

@Singleton
internal class TenantDomainStore @Inject constructor(jdbi: Jdbi) : SqlStore(jdbi) {
  fun create(model: TenantDomainModel): TenantDomainModel =
      withHandle { handle ->
        try {
          handle.createQuery(sqlResource("store/tenantDomain/create.sql"))
              .bindKotlin(model)
              .mapTo(TenantDomainModel::class.java)
              .single()
        } catch (e: UnableToExecuteStatementException) {
          handleCreateError(e)
        }
      }

  fun getByOrgGuid(orgGuid: UUID): Set<TenantDomainModel> =
      withHandle { handle ->
        handle.createQuery(sqlResource("store/tenantDomain/getByOrgGuid.sql"))
            .bind("orgGuid", orgGuid)
            .mapTo(TenantDomainModel::class.java)
            .toSet()
      }

  fun delete(orgGuid: UUID, domain: String): Unit =
      inTransaction { handle ->
        handle.createUpdate(sqlResource("store/tenantDomain/delete.sql"))
            .bind("orgGuid", orgGuid)
            .bind("domain", domain)
            .update() ?: throw TenantDomainNotFound()
      }

  private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isForeignKeyViolation(FK_ORG_GUID) -> throw TenantNotFound().unprocessable()
      error.isUniqueConstraintViolation(UNIQ_DOMAIN) -> throw TenantDomainAlreadyRegistered()
      else -> throw e
    }
  }
}
