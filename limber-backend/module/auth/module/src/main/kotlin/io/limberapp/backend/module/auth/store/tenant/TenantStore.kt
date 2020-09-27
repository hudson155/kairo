package io.limberapp.backend.module.auth.store.tenant

import com.google.inject.Inject
import com.google.inject.Singleton
import io.limberapp.backend.module.auth.exception.tenant.Auth0ClientIdAlreadyRegistered
import io.limberapp.backend.module.auth.exception.tenant.OrgAlreadyHasTenant
import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.model.tenant.TenantModel
import io.limberapp.common.store.SqlStore
import io.limberapp.common.store.isUniqueConstraintViolation
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*

private const val UNIQ_ORG_GUID = "uniq__tenant__org_guid"
private const val UNIQ_AUTH0_CLIENT_ID = "uniq__tenant__auth0_client_id"

@Singleton
internal class TenantStore @Inject constructor(jdbi: Jdbi) : SqlStore(jdbi) {
  fun create(model: TenantModel): TenantModel =
    withHandle { handle ->
      try {
        handle.createQuery(sqlResource("/store/tenant/create.sql"))
          .bindKotlin(model)
          .mapTo(TenantModel::class.java)
          .single()
      } catch (e: UnableToExecuteStatementException) {
        handleCreateError(e)
      }
    }

  fun get(orgGuid: UUID): TenantModel? =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/tenant/get.sql"))
        .bind("orgGuid", orgGuid)
        .mapTo(TenantModel::class.java)
        .singleNullOrThrow()
    }

  fun getByAuth0ClientId(auth0ClientId: String): TenantModel? =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/tenant/getByAuth0ClientId.sql"))
        .bind("auth0ClientId", auth0ClientId)
        .mapTo(TenantModel::class.java)
        .singleNullOrThrow()
    }

  fun getByDomain(domain: String): TenantModel? =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/tenant/getByDomain.sql"))
        .bind("domain", domain)
        .mapTo(TenantModel::class.java)
        .singleNullOrThrow()
    }

  fun update(orgGuid: UUID, update: TenantModel.Update): TenantModel =
    inTransaction { handle ->
      try {
        handle.createQuery(sqlResource("/store/tenant/update.sql"))
          .bind("orgGuid", orgGuid)
          .bindKotlin(update)
          .mapTo(TenantModel::class.java)
          .singleNullOrThrow()
      } catch (e: UnableToExecuteStatementException) {
        handleUpdateError(e)
      } ?: throw TenantNotFound()
    }

  fun delete(orgGuid: UUID): Unit =
    inTransaction { handle ->
      handle.createUpdate(sqlResource("/store/tenant/delete.sql"))
        .bind("orgGuid", orgGuid)
        .updateOnly() ?: throw TenantNotFound()
    }

  private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isUniqueConstraintViolation(UNIQ_ORG_GUID) -> throw OrgAlreadyHasTenant()
      error.isUniqueConstraintViolation(UNIQ_AUTH0_CLIENT_ID) -> throw Auth0ClientIdAlreadyRegistered()
      else -> throw e
    }
  }

  private fun handleUpdateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isUniqueConstraintViolation(UNIQ_AUTH0_CLIENT_ID) -> throw Auth0ClientIdAlreadyRegistered()
      else -> throw e
    }
  }
}
