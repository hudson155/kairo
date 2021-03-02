package io.limberapp.store.org

import com.google.inject.Inject
import com.google.inject.Singleton
import io.limberapp.exception.org.OrgRoleNameIsNotUnique
import io.limberapp.exception.org.OrgRoleNotFound
import io.limberapp.model.org.OrgRoleModel
import io.limberapp.sql.store.SqlStore
import io.limberapp.sql.store.isUniqueConstraintViolation
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.UUID

private const val UNIQ_NAME = "uniq__org_role__name"

@Singleton
internal class OrgRoleStore @Inject constructor(jdbi: Jdbi) : SqlStore(jdbi) {
  fun create(model: OrgRoleModel): OrgRoleModel =
      withHandle { handle ->
        try {
          handle.createQuery(sqlResource("store/orgRole/create.sql"))
              .bindKotlin(model)
              .mapTo(OrgRoleModel::class.java)
              .single()
        } catch (e: UnableToExecuteStatementException) {
          handleCreateError(e)
        }
      }

  operator fun get(orgRoleGuid: UUID): OrgRoleModel? =
      withHandle { handle ->
        handle.createQuery(sqlResource("store/orgRole/get.sql"))
            .bind("orgRoleGuid", orgRoleGuid)
            .mapTo(OrgRoleModel::class.java)
            .singleNullOrThrow()
      }

  fun getByUserGuid(orgGuid: UUID, userGuid: UUID): Set<OrgRoleModel> =
      withHandle { handle ->
        handle.createQuery(sqlResource("store/orgRole/getByUserGuid.sql"))
            .bind("orgGuid", orgGuid)
            .bind("userGuid", userGuid)
            .mapTo(OrgRoleModel::class.java)
            .toSet()
      }

  fun getByOrgGuid(orgGuid: UUID): Set<OrgRoleModel> =
      withHandle { handle ->
        handle.createQuery(sqlResource("store/orgRole/getByOrgGuid.sql"))
            .bind("orgGuid", orgGuid)
            .mapTo(OrgRoleModel::class.java)
            .toSet()
      }

  fun update(orgRoleGuid: UUID, update: OrgRoleModel.Update): OrgRoleModel =
      inTransaction { handle ->
        try {
          handle.createQuery(sqlResource("store/orgRole/update.sql"))
              .bind("orgRoleGuid", orgRoleGuid)
              .bindKotlin(update)
              .mapTo(OrgRoleModel::class.java)
              .singleNullOrThrow() ?: throw OrgRoleNotFound()
        } catch (e: UnableToExecuteStatementException) {
          handleUpdateError(e)
        }
      }

  fun delete(orgRoleGuid: UUID): Unit =
      inTransaction { handle ->
        handle.createUpdate(sqlResource("store/orgRole/delete.sql"))
            .bind("orgRoleGuid", orgRoleGuid)
            .singleNullOrThrow() ?: throw OrgRoleNotFound()
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
