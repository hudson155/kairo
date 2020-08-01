package io.limberapp.backend.module.auth.store.org

import com.google.inject.Inject
import com.google.inject.Singleton
import com.piperframework.store.SqlStore
import com.piperframework.store.isUniqueConstraintViolation
import io.limberapp.backend.module.auth.exception.org.OrgRoleNameIsNotUnique
import io.limberapp.backend.module.auth.exception.org.OrgRoleNotFound
import io.limberapp.backend.module.auth.model.org.OrgRoleModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*

private const val ORG_ROLE_NAME_UNIQUE_CONSTRAINT = "org_role_org_guid_lower_idx"

@Singleton
internal class OrgRoleStore @Inject constructor(jdbi: Jdbi) : SqlStore(jdbi) {
  fun create(model: OrgRoleModel): OrgRoleModel =
    withHandle { handle ->
      return@withHandle try {
        handle.createQuery(sqlResource("/store/orgRole/create.sql"))
          .bindKotlin(model)
          .mapTo(OrgRoleModel::class.java)
          .one()
      } catch (e: UnableToExecuteStatementException) {
        handleCreateError(e)
      }
    }

  fun get(orgGuid: UUID, orgRoleGuid: UUID): OrgRoleModel? =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/orgRole/get.sql"))
        .bind("orgGuid", orgGuid)
        .bind("orgRoleGuid", orgRoleGuid)
        .mapTo(OrgRoleModel::class.java)
        .findOne().orElse(null)
    }

  fun getByOrgGuid(orgGuid: UUID): Set<OrgRoleModel> =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/orgRole/getByOrgGuid.sql"))
        .bind("orgGuid", orgGuid)
        .mapTo(OrgRoleModel::class.java)
        .toSet()
    }

  fun getByAccountGuid(orgGuid: UUID, accountGuid: UUID): Set<OrgRoleModel> =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/orgRole/getByAccountGuid.sql"))
        .bind("orgGuid", orgGuid)
        .bind("accountGuid", accountGuid)
        .mapTo(OrgRoleModel::class.java)
        .toSet()
    }

  fun update(orgGuid: UUID, orgRoleGuid: UUID, update: OrgRoleModel.Update): OrgRoleModel =
    inTransaction {
      val updateCount = try {
        it.createUpdate(sqlResource("/store/orgRole/update.sql"))
          .bind("orgGuid", orgGuid)
          .bind("orgRoleGuid", orgRoleGuid)
          .bindKotlin(update)
          .execute()
      } catch (e: UnableToExecuteStatementException) {
        handleUpdateError(e)
      }
      return@inTransaction when (updateCount) {
        0 -> throw OrgRoleNotFound()
        1 -> checkNotNull(get(orgGuid, orgRoleGuid))
        else -> badSql()
      }
    }

  fun delete(orgGuid: UUID, orgRoleGuid: UUID) =
    inTransaction {
      val updateCount = it.createUpdate(sqlResource("/store/orgRole/delete.sql"))
        .bind("orgGuid", orgGuid)
        .bind("orgRoleGuid", orgRoleGuid)
        .execute()
      return@inTransaction when (updateCount) {
        0 -> throw OrgRoleNotFound()
        1 -> Unit
        else -> badSql()
      }
    }

  private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isUniqueConstraintViolation(ORG_ROLE_NAME_UNIQUE_CONSTRAINT) -> throw OrgRoleNameIsNotUnique()
      else -> throw e
    }
  }

  private fun handleUpdateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isUniqueConstraintViolation(ORG_ROLE_NAME_UNIQUE_CONSTRAINT) -> throw OrgRoleNameIsNotUnique()
      else -> throw e
    }
  }
}
