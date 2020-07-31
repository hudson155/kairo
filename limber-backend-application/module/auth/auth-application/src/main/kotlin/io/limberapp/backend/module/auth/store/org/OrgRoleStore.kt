package io.limberapp.backend.module.auth.store.org

import com.google.inject.Inject
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

internal class OrgRoleStore @Inject constructor(private val jdbi: Jdbi) : SqlStore(jdbi) {
  fun create(model: OrgRoleModel): OrgRoleModel {
    return jdbi.withHandle<OrgRoleModel, Exception> {
      try {
        it.createQuery(sqlResource("/store/orgRole/create.sql"))
          .bindKotlin(model)
          .mapTo(OrgRoleModel::class.java)
          .single()
      } catch (e: UnableToExecuteStatementException) {
        handleCreateError(e)
      }
    }
  }

  private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isUniqueConstraintViolation(ORG_ROLE_NAME_UNIQUE_CONSTRAINT) -> throw OrgRoleNameIsNotUnique()
      else -> throw e
    }
  }

  fun get(orgGuid: UUID? = null, orgRoleGuid: UUID? = null, accountGuid: UUID? = null): List<OrgRoleModel> {
    return jdbi.withHandle<List<OrgRoleModel>, Exception> {
      it.createQuery(sqlResource("/store/orgRole/get.sql")).build {
        if (orgGuid != null) {
          conditions.add("org_guid = :orgGuid")
          bindings["orgGuid"] = orgGuid
        }
        if (orgRoleGuid != null) {
          conditions.add("guid = :orgRoleGuid")
          bindings["orgRoleGuid"] = orgRoleGuid
        }
        if (accountGuid != null) {
          conditions.add(
            """
            EXISTS(SELECT id
                   FROM auth.org_role_membership
                   WHERE org_role_guid = org_role.guid
                     AND account_guid = :accountGuid)
            """.trimIndent()
          )
          bindings["accountGuid"] = accountGuid
        }
      }
        .mapTo(OrgRoleModel::class.java)
        .list()
    }
  }

  fun update(orgRoleGuid: UUID, update: OrgRoleModel.Update): OrgRoleModel {
    return jdbi.inTransaction<OrgRoleModel, Exception> {
      val updateCount = try {
        it.createUpdate(sqlResource("/store/orgRole/update.sql"))
          .bind("guid", orgRoleGuid)
          .bindKotlin(update)
          .execute()
      } catch (e: UnableToExecuteStatementException) {
        handleUpdateError(e)
      }
      return@inTransaction when (updateCount) {
        0 -> throw OrgRoleNotFound()
        1 -> get(orgRoleGuid = orgRoleGuid).single()
        else -> badSql()
      }
    }
  }

  private fun handleUpdateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isUniqueConstraintViolation(ORG_ROLE_NAME_UNIQUE_CONSTRAINT) -> throw OrgRoleNameIsNotUnique()
      else -> throw e
    }
  }

  fun delete(orgRoleGuid: UUID) {
    jdbi.useTransaction<Exception> {
      val updateCount = it.createUpdate(
        """
        UPDATE auth.org_role
        SET archived_date = NOW()
        WHERE guid = :guid
          AND archived_date IS NULL
        """.trimIndent()
      )
        .bind("guid", orgRoleGuid)
        .execute()
      return@useTransaction when (updateCount) {
        0 -> throw OrgRoleNotFound()
        1 -> Unit
        else -> badSql()
      }
    }
  }
}
