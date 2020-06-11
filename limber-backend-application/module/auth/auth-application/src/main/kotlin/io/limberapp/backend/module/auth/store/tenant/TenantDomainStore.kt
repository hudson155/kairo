package io.limberapp.backend.module.auth.store.tenant

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.store.isForeignKeyViolation
import com.piperframework.store.isUniqueConstraintViolation
import com.piperframework.util.singleNullOrThrow
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

internal class TenantDomainStore @Inject constructor(private val jdbi: Jdbi) : SqlStore() {
  fun create(model: TenantDomainModel): TenantDomainModel {
    return jdbi.withHandle<TenantDomainModel, Exception> {
      try {
        it.createQuery(sqlResource("create"))
          .bindKotlin(model)
          .mapTo(TenantDomainModel::class.java)
          .single()
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

  fun existsAndHasOrgGuid(domain: String, orgGuid: UUID): Boolean {
    val model = get(domain) ?: return false
    return model.orgGuid == orgGuid
  }

  fun get(domain: String): TenantDomainModel? {
    return jdbi.withHandle<TenantDomainModel?, Exception> {
      it.createQuery("SELECT * FROM auth.tenant_domain WHERE LOWER(domain) = LOWER(:domain)")
        .bind("domain", domain)
        .mapTo(TenantDomainModel::class.java)
        .singleNullOrThrow()
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

  fun delete(domain: String) {
    return jdbi.useTransaction<Exception> {
      val updateCount =
        it.createUpdate("DELETE FROM auth.tenant_domain WHERE LOWER(domain) = LOWER(:domain)")
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
