package io.limberapp.backend.module.auth.store.tenant

import com.google.inject.Inject
import com.google.inject.Singleton
import com.piperframework.finder.Finder
import com.piperframework.store.SqlStore
import com.piperframework.store.isUniqueConstraintViolation
import com.piperframework.store.withFinder
import io.limberapp.backend.module.auth.exception.tenant.Auth0ClientIdAlreadyRegistered
import io.limberapp.backend.module.auth.exception.tenant.OrgAlreadyHasTenant
import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.model.tenant.TenantFinder
import io.limberapp.backend.module.auth.model.tenant.TenantModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*

private const val UNIQ_ORG_GUID = "uniq__tenant__org_guid"
private const val UNIQ_AUTH0_CLIENT_ID = "uniq__tenant__auth0_client_id"

@Singleton
internal class TenantStore @Inject constructor(jdbi: Jdbi) : SqlStore(jdbi), Finder<TenantModel, TenantFinder> {
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

  override fun <R> find(result: (Iterable<TenantModel>) -> R, query: TenantFinder.() -> Unit): R =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/tenant/find.sql"))
        .withFinder(TenantQueryBuilder().apply(query))
        .mapTo(TenantModel::class.java)
        .let(result)
    }

  fun update(orgGuid: UUID, update: TenantModel.Update): TenantModel =
    inTransaction { handle ->
      val updateCount = try {
        handle.createUpdate(sqlResource("/store/tenant/update.sql"))
          .bind("orgGuid", orgGuid)
          .bindKotlin(update)
          .execute()
      } catch (e: UnableToExecuteStatementException) {
        handleUpdateError(e)
      }
      return@inTransaction when (updateCount) {
        0 -> throw TenantNotFound()
        1 -> findOnlyOrThrow { orgGuid(orgGuid) }
        else -> badSql()
      }
    }

  fun delete(orgGuid: UUID) =
    inTransaction { handle ->
      val updateCount = handle.createUpdate(sqlResource("/store/tenant/delete.sql"))
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
