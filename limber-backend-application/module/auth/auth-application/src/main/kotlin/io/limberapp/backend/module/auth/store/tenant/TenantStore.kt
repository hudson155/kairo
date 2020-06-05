package io.limberapp.backend.module.auth.store.tenant

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.store.isUniqueConstraintViolation
import com.piperframework.util.singleNullOrThrow
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

internal class TenantStore @Inject constructor(private val jdbi: Jdbi) : SqlStore() {
  fun create(model: TenantModel): TenantModel {
    return jdbi.withHandle<TenantModel, Exception> {
      try {
        it.createQuery(sqlResource("create"))
          .bindKotlin(model)
          .mapTo(TenantModel::class.java)
          .single()
      } catch (e: UnableToExecuteStatementException) {
        handleCreateError(e)
      }
    }
  }

  private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isUniqueConstraintViolation(ORG_GUID_UNIQUE_CONSTRAINT) ->
        throw OrgAlreadyHasTenant()
      error.isUniqueConstraintViolation(AUTH0_CLIENT_ID_UNIQUE_CONSTRAINT) ->
        throw Auth0ClientIdAlreadyRegistered()
      else -> throw e
    }
  }

  fun get(orgGuid: UUID): TenantModel? {
    return jdbi.withHandle<TenantModel?, Exception> {
      it.createQuery("SELECT * FROM auth.tenant WHERE org_guid = :orgGuid AND archived_date IS NULL")
        .bind("orgGuid", orgGuid)
        .mapTo(TenantModel::class.java)
        .singleNullOrThrow()
    }
  }

  fun getByDomain(domain: String): TenantModel? {
    return jdbi.withHandle<TenantModel?, Exception> {
      it.createQuery(
          """
                    SELECT *
                    FROM auth.tenant
                    WHERE org_guid = (SELECT org_guid
                                      FROM auth.tenant_domain
                                      WHERE LOWER(domain) = LOWER(:domain))
                      AND archived_date IS NULL
                    """.trimIndent()
        )
        .bind("domain", domain)
        .mapTo(TenantModel::class.java)
        .singleNullOrThrow()
    }
  }

  fun getByAuth0ClientId(auth0ClientId: String): TenantModel? {
    return jdbi.withHandle<TenantModel?, Exception> {
      it.createQuery("SELECT * FROM auth.tenant WHERE auth0_client_id = :auth0ClientId AND archived_date IS NULL")
        .bind("auth0ClientId", auth0ClientId)
        .mapTo(TenantModel::class.java)
        .singleNullOrThrow()
    }
  }

  fun update(orgGuid: UUID, update: TenantModel.Update): TenantModel {
    return jdbi.inTransaction<TenantModel, Exception> {
      val updateCount = try {
        it.createUpdate(sqlResource("update"))
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
  }

  private fun handleUpdateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    if (error.isUniqueConstraintViolation(AUTH0_CLIENT_ID_UNIQUE_CONSTRAINT)) {
      throw Auth0ClientIdAlreadyRegistered()
    }
    throw e
  }

  fun delete(orgGuid: UUID) {
    jdbi.useTransaction<Exception> {
      val updateCount = it.createUpdate(
          """
                    UPDATE auth.tenant
                    SET archived_date = NOW()
                    WHERE org_guid = :orgGuid
                      AND archived_date IS NULL
                    """.trimIndent()
        )
        .bind("orgGuid", orgGuid)
        .execute()
      return@useTransaction when (updateCount) {
        0 -> throw TenantNotFound()
        1 -> Unit
        else -> badSql()
      }
    }
  }
}
