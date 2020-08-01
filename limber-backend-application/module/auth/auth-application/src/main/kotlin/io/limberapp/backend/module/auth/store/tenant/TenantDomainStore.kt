package io.limberapp.backend.module.auth.store.tenant

import com.google.inject.Inject
import com.google.inject.Singleton
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
import java.util.*

private const val ORG_GUID_FOREIGN_KEY = "tenant_domain_org_guid_fkey"
private const val DOMAIN_UNIQUE_CONSTRAINT = "tenant_domain_lower_idx"

@Singleton
internal class TenantDomainStore @Inject constructor(jdbi: Jdbi) : SqlStore(jdbi) {
  fun create(model: TenantDomainModel): TenantDomainModel =
    withHandle { handle ->
      return@withHandle try {
        handle.createQuery(sqlResource("/store/tenantDomain/create.sql"))
          .bindKotlin(model)
          .mapTo(TenantDomainModel::class.java)
          .one()
      } catch (e: UnableToExecuteStatementException) {
        handleCreateError(e)
      }
    }

  fun get(orgGuid: UUID, domain: String): TenantDomainModel? =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/tenantDomain/get.sql"))
        .bind("orgGuid", orgGuid)
        .bind("domain", domain)
        .mapTo(TenantDomainModel::class.java)
        .findOne().orElse(null)
    }

  fun getByOrgGuid(orgGuid: UUID): Set<TenantDomainModel> =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/tenantDomain/getByOrgGuid.sql"))
        .bind("orgGuid", orgGuid)
        .mapTo(TenantDomainModel::class.java)
        .toSet()
    }

  fun delete(orgGuid: UUID, domain: String) =
    inTransaction {
      val updateCount =
        it.createUpdate(sqlResource("/store/tenantDomain/delete.sql"))
          .bind("orgGuid", orgGuid)
          .bind("domain", domain)
          .execute()
      return@inTransaction when (updateCount) {
        0 -> throw TenantDomainNotFound()
        1 -> Unit
        else -> badSql()
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
}
