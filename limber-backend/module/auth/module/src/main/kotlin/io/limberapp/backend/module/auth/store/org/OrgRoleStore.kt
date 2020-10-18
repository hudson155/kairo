package io.limberapp.backend.module.auth.store.org

import com.google.inject.Inject
import com.google.inject.Singleton
import io.limberapp.backend.module.auth.exception.org.OrgRoleNameIsNotUnique
import io.limberapp.backend.module.auth.exception.org.OrgRoleNotFound
import io.limberapp.backend.module.auth.model.org.OrgRoleModel
import io.limberapp.common.store.SqlStore
import io.limberapp.common.store.isUniqueConstraintViolation
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*

private const val UNIQ_NAME = "uniq__org_role__name"

@Singleton
internal class OrgRoleStore @Inject constructor(jdbi: Jdbi) : SqlStore(jdbi) {
  fun create(model: OrgRoleModel): OrgRoleModel =
      withHandle { handle ->
        try {
          handle.createQuery(sqlResource("/store/orgRole/create.sql"))
              .bindKotlin(model)
              .mapTo(OrgRoleModel::class.java)
              .single()
        } catch (e: UnableToExecuteStatementException) {
          handleCreateError(e)
        }
      }

  fun getByAccountGuid(orgGuid: UUID, accountGuid: UUID): Set<OrgRoleModel> =
      withHandle { handle ->
        handle.createQuery(sqlResource("/store/orgRole/getByAccountGuid.sql"))
            .bind("orgGuid", orgGuid)
            .bind("accountGuid", accountGuid)
            .mapTo(OrgRoleModel::class.java)
            .toSet()
      }

  fun getByOrgGuid(orgGuid: UUID): Set<OrgRoleModel> =
      withHandle { handle ->
        handle.createQuery(sqlResource("/store/orgRole/getByOrgGuid.sql"))
            .bind("orgGuid", orgGuid)
            .mapTo(OrgRoleModel::class.java)
            .toSet()
      }

  fun update(orgGuid: UUID, orgRoleGuid: UUID, update: OrgRoleModel.Update): OrgRoleModel =
      inTransaction { handle ->
        try {
          handle.createQuery(sqlResource("/store/orgRole/update.sql"))
              .bind("orgGuid", orgGuid)
              .bind("orgRoleGuid", orgRoleGuid)
              .bindKotlin(update)
              .mapTo(OrgRoleModel::class.java)
              .singleNullOrThrow()
        } catch (e: UnableToExecuteStatementException) {
          handleUpdateError(e)
        } ?: throw OrgRoleNotFound()
      }

  fun delete(orgGuid: UUID, orgRoleGuid: UUID): Unit =
      inTransaction { handle ->
        handle.createUpdate(sqlResource("/store/orgRole/delete.sql"))
            .bind("orgGuid", orgGuid)
            .bind("orgRoleGuid", orgRoleGuid)
            .updateOnly() ?: throw OrgRoleNotFound()
      }

  private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isUniqueConstraintViolation(UNIQ_NAME) -> throw OrgRoleNameIsNotUnique()
      else -> throw e
    }
  }

  private fun handleUpdateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isUniqueConstraintViolation(UNIQ_NAME) -> throw OrgRoleNameIsNotUnique()
      else -> throw e
    }
  }
}
