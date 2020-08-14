package io.limberapp.backend.module.auth.store.org

import com.google.inject.Inject
import com.google.inject.Singleton
import com.piperframework.finder.Finder
import com.piperframework.store.SqlStore
import com.piperframework.store.isForeignKeyViolation
import com.piperframework.store.isUniqueConstraintViolation
import com.piperframework.store.withFinder
import io.limberapp.backend.module.auth.exception.org.AccountIsAlreadyMemberOfOrgRole
import io.limberapp.backend.module.auth.exception.org.OrgRoleMembershipNotFound
import io.limberapp.backend.module.auth.exception.org.OrgRoleNotFound
import io.limberapp.backend.module.auth.model.org.OrgRoleMembershipFinder
import io.limberapp.backend.module.auth.model.org.OrgRoleMembershipModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*

private const val ORG_ROLE_GUID_FOREIGN_KEY = "org_role_membership_org_role_guid_fkey"
private const val ORG_ROLE_GUID_ACCOUNT_GUID_UNIQUE_CONSTRAINT = "org_role_membership_org_role_guid_account_guid_key"

@Singleton
internal class OrgRoleMembershipStore @Inject constructor(
  jdbi: Jdbi,
) : SqlStore(jdbi), Finder<OrgRoleMembershipModel, OrgRoleMembershipFinder> {
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

  override fun <R> find(result: (Iterable<OrgRoleMembershipModel>) -> R, query: OrgRoleMembershipFinder.() -> Unit): R =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/orgRoleMembership/find.sql"))
        .withFinder(OrgRoleMembershipQueryBuilder().apply(query))
        .mapTo(OrgRoleMembershipModel::class.java)
        .let(result)
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
      error.isForeignKeyViolation(ORG_ROLE_GUID_FOREIGN_KEY) ->
        throw OrgRoleNotFound()
      error.isUniqueConstraintViolation(ORG_ROLE_GUID_ACCOUNT_GUID_UNIQUE_CONSTRAINT) ->
        throw AccountIsAlreadyMemberOfOrgRole()
      else -> throw e
    }
  }
}
