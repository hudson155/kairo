package io.limberapp.store.org

import com.google.inject.Inject
import com.google.inject.Singleton
import io.limberapp.exception.org.UserIsAlreadyMemberOfOrgRole
import io.limberapp.exception.org.OrgRoleMembershipNotFound
import io.limberapp.exception.org.OrgRoleNotFound
import io.limberapp.exception.unprocessable
import io.limberapp.model.org.OrgRoleMembershipModel
import io.limberapp.sql.store.SqlStore
import io.limberapp.sql.store.isForeignKeyViolation
import io.limberapp.sql.store.isUniqueConstraintViolation
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.UUID

private const val FK_ORG_ROLE_GUID = "fk__org_role_membership__org_role_guid"
private const val UNIQ_USER_GUID = "uniq__org_role_membership__user_guid"

@Singleton
internal class OrgRoleMembershipStore @Inject constructor(jdbi: Jdbi) : SqlStore(jdbi) {
  fun create(model: OrgRoleMembershipModel): OrgRoleMembershipModel =
      withHandle { handle ->
        try {
          handle.createQuery(sqlResource("store/orgRoleMembership/create.sql"))
              .bindKotlin(model)
              .mapTo(OrgRoleMembershipModel::class.java)
              .single()
        } catch (e: UnableToExecuteStatementException) {
          handleCreateError(e)
        }
      }

  fun getByOrgRoleGuid(orgRoleGuid: UUID): Set<OrgRoleMembershipModel> =
      withHandle { handle ->
        handle.createQuery(sqlResource("store/orgRoleMembership/getByOrgRoleGuid.sql"))
            .bind("orgRoleGuid", orgRoleGuid)
            .mapTo(OrgRoleMembershipModel::class.java)
            .toSet()
      }

  fun delete(orgRoleGuid: UUID, userGuid: UUID): Unit =
      inTransaction { handle ->
        handle.createUpdate(sqlResource("store/orgRoleMembership/delete.sql"))
            .bind("orgRoleGuid", orgRoleGuid)
            .bind("userGuid", userGuid)
            .singleNullOrThrow() ?: throw OrgRoleMembershipNotFound()
      }

  private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isForeignKeyViolation(FK_ORG_ROLE_GUID) -> throw OrgRoleNotFound().unprocessable()
      error.isUniqueConstraintViolation(UNIQ_USER_GUID) -> throw UserIsAlreadyMemberOfOrgRole()
      else -> throw e
    }
  }
}
