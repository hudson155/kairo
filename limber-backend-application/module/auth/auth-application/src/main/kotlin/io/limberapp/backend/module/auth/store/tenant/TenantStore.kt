package io.limberapp.backend.module.auth.store.tenant

import com.google.inject.Inject
import com.google.inject.Singleton
import com.piperframework.store.SqlStore
import com.piperframework.store.isUniqueConstraintViolation
import io.limberapp.backend.module.auth.exception.tenant.Auth0ClientIdAlreadyRegistered
import io.limberapp.backend.module.auth.exception.tenant.OrgAlreadyHasTenant
import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.model.tenant.TenantModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*

private const val ORG_GUID_UNIQUE_CONSTRAINT = "tenant_org_guid_key"
private const val AUTH0_CLIENT_ID_UNIQUE_CONSTRAINT = "tenant_auth0_client_id_key"

@Singleton
internal class TenantStore @Inject constructor(jdbi: Jdbi) : SqlStore(jdbi) {
  fun create(model: TenantModel): TenantModel =
    withHandle { handle ->
      return@withHandle try {
        handle.createQuery(sqlResource("/store/tenant/create.sql"))
          .bindKotlin(model)
          .mapTo(TenantModel::class.java)
          .one()
      } catch (e: UnableToExecuteStatementException) {
        handleCreateError(e)
      }
    }

  fun get(orgGuid: UUID): TenantModel? =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/tenant/get.sql"))
        .bind("orgGuid", orgGuid)
        .mapTo(TenantModel::class.java)
        .findOne().orElse(null)
    }

  fun getByAuth0ClientId(auth0ClientId: String): TenantModel? =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/tenant/getByAuth0ClientId.sql"))
        .bind("auth0ClientId", auth0ClientId)
        .mapTo(TenantModel::class.java)
        .findOne().orElse(null)
    }

  fun getByDomain(domain: String): TenantModel? =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/tenant/getByDomain.sql"))
        .bind("domain", domain)
        .mapTo(TenantModel::class.java)
        .findOne().orElse(null)
    }

  fun update(orgGuid: UUID, update: TenantModel.Update): TenantModel =
    inTransaction {
      val updateCount = try {
        it.createUpdate(sqlResource("/store/tenant/update.sql"))
          .bind("orgGuid", orgGuid)
          .bindKotlin(update)
          .execute()
      } catch (e: UnableToExecuteStatementException) {
        handleUpdateError(e)
      }
      return@inTransaction when (updateCount) {
        0 -> throw TenantNotFound()
        1 -> checkNotNull(get(orgGuid))
        else -> badSql()
      }
    }

  fun delete(orgGuid: UUID) =
    inTransaction {
      val updateCount = it.createUpdate(sqlResource("/store/tenant/delete.sql"))
        .bind("orgGuid", orgGuid)
        .execute()
      return@inTransaction when (updateCount) {
        0 -> throw TenantNotFound()
        1 -> Unit
        else -> badSql()
      }
    }

  private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isUniqueConstraintViolation(ORG_GUID_UNIQUE_CONSTRAINT) -> throw OrgAlreadyHasTenant()
      error.isUniqueConstraintViolation(AUTH0_CLIENT_ID_UNIQUE_CONSTRAINT) -> throw Auth0ClientIdAlreadyRegistered()
      else -> throw e
    }
  }

  private fun handleUpdateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isUniqueConstraintViolation(AUTH0_CLIENT_ID_UNIQUE_CONSTRAINT) -> throw Auth0ClientIdAlreadyRegistered()
      else -> throw e
    }
  }
}
