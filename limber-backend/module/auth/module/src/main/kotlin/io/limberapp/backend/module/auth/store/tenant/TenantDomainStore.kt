package io.limberapp.backend.module.auth.store.tenant

import com.google.inject.Inject
import com.google.inject.Singleton
import io.limberapp.backend.module.auth.exception.tenant.TenantDomainAlreadyRegistered
import io.limberapp.backend.module.auth.exception.tenant.TenantDomainNotFound
import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.model.tenant.TenantDomainFinder
import io.limberapp.backend.module.auth.model.tenant.TenantDomainModel
import io.limberapp.common.finder.Finder
import io.limberapp.common.store.SqlStore
import io.limberapp.common.store.isForeignKeyViolation
import io.limberapp.common.store.isUniqueConstraintViolation
import io.limberapp.common.store.withFinder
import io.limberapp.exception.unprocessableEntity.unprocessable
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*

private const val FK_ORG_GUID = "fk__tenant_domain__org_guid"
private const val UNIQ_DOMAIN = "uniq__tenant_domain__domain"

@Singleton
internal class TenantDomainStore @Inject constructor(
  jdbi: Jdbi,
) : SqlStore(jdbi), Finder<TenantDomainModel, TenantDomainFinder> {
  fun create(model: TenantDomainModel): TenantDomainModel =
    withHandle { handle ->
      try {
        handle.createQuery(sqlResource("/store/tenantDomain/create.sql"))
          .bindKotlin(model)
          .mapTo(TenantDomainModel::class.java)
          .single()
      } catch (e: UnableToExecuteStatementException) {
        handleCreateError(e)
      }
    }

  override fun <R> find(result: (Iterable<TenantDomainModel>) -> R, query: TenantDomainFinder.() -> Unit): R =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/tenantDomain/find.sql"))
        .withFinder(TenantDomainQueryBuilder().apply(query))
        .mapTo(TenantDomainModel::class.java)
        .let(result)
    }

  fun delete(orgGuid: UUID, domain: String): Unit =
    inTransaction { handle ->
      handle.createUpdate(sqlResource("/store/tenantDomain/delete.sql"))
        .bind("orgGuid", orgGuid)
        .bind("domain", domain)
        .updateOnly() ?: throw TenantDomainNotFound()
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
