package io.limberapp.backend.module.auth.store.org

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.store.isNotNullConstraintViolation
import com.piperframework.store.isUniqueConstraintViolation
import io.limberapp.backend.module.auth.exception.org.AccountIsAlreadyMemberOfOrgRole
import io.limberapp.backend.module.auth.exception.org.OrgRoleMembershipNotFound
import io.limberapp.backend.module.auth.exception.org.OrgRoleNotFound
import io.limberapp.backend.module.auth.model.org.OrgRoleMembershipModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*

private const val ORG_ROLE_GUID_ACCOUNT_GUID_UNIQUE_CONSTRAINT = "org_role_membership_org_role_guid_account_guid_key"

internal class OrgRoleMembershipStore @Inject constructor(jdbi: Jdbi) : SqlStore(jdbi) {
  fun create(orgGuid: UUID, model: OrgRoleMembershipModel): OrgRoleMembershipModel =
    withHandle { handle ->
      return@withHandle try {
        handle.createQuery(sqlResource("/store/orgRoleMembership/create.sql"))
          .bind("orgGuid", orgGuid)
          .bindKotlin(model)
          .mapTo(OrgRoleMembershipModel::class.java)
          .one()
      } catch (e: UnableToExecuteStatementException) {
        handleCreateError(e)
      }
    }

  fun get(orgGuid: UUID, orgRoleGuid: UUID): Set<OrgRoleMembershipModel> =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/orgRoleMembership/get.sql"))
        .bind("orgGuid", orgGuid)
        .bind("orgRoleGuid", orgRoleGuid)
        .mapTo(OrgRoleMembershipModel::class.java)
        .toSet()
    }

  fun delete(orgGuid: UUID, orgRoleGuid: UUID, accountGuid: UUID) =
    inTransaction { handle ->
      val updateCount = handle.createUpdate(sqlResource("/store/orgRoleMembership/delete.sql"))
        .bind("orgGuid", orgGuid)
        .bind("orgRoleGuid", orgRoleGuid)
        .bind("accountGuid", accountGuid)
        .execute()
      return@inTransaction when (updateCount) {
        0 -> throw OrgRoleMembershipNotFound()
        1 -> Unit
        else -> badSql()
      }
    }

  private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isNotNullConstraintViolation("org_role_guid") ->
        throw OrgRoleNotFound()
      error.isUniqueConstraintViolation(ORG_ROLE_GUID_ACCOUNT_GUID_UNIQUE_CONSTRAINT) ->
        throw AccountIsAlreadyMemberOfOrgRole()
      else -> throw e
    }
  }
}
