package io.limberapp.backend.module.auth.store.org

import com.google.inject.Inject
import com.google.inject.Singleton
import io.limberapp.backend.module.auth.exception.org.AccountIsAlreadyMemberOfOrgRole
import io.limberapp.backend.module.auth.exception.org.OrgRoleMembershipNotFound
import io.limberapp.backend.module.auth.exception.org.OrgRoleNotFound
import io.limberapp.backend.module.auth.model.org.OrgRoleMembershipFinder
import io.limberapp.backend.module.auth.model.org.OrgRoleMembershipModel
import io.limberapp.common.finder.Finder
import io.limberapp.common.store.SqlStore
import io.limberapp.common.store.isForeignKeyViolation
import io.limberapp.common.store.isUniqueConstraintViolation
import io.limberapp.common.store.withFinder
import io.limberapp.exception.unprocessableEntity.unprocessable
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*

private const val FK_ORG_ROLE_GUID = "fk__org_role_membership__org_role_guid"
private const val UNIQ_ACCOUNT_GUID = "uniq__org_role_membership__account_guid"

@Singleton
internal class OrgRoleMembershipStore @Inject constructor(
  jdbi: Jdbi,
) : SqlStore(jdbi), Finder<OrgRoleMembershipModel, OrgRoleMembershipFinder> {
  fun create(orgGuid: UUID, model: OrgRoleMembershipModel): OrgRoleMembershipModel =
    withHandle { handle ->
      try {
        handle.createQuery(sqlResource("/store/orgRoleMembership/create.sql"))
          .bind("orgGuid", orgGuid)
          .bindKotlin(model)
          .mapTo(OrgRoleMembershipModel::class.java)
          .single()
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

  fun delete(orgGuid: UUID, orgRoleGuid: UUID, accountGuid: UUID): Unit =
    inTransaction { handle ->
      handle.createUpdate(sqlResource("/store/orgRoleMembership/delete.sql"))
        .bind("orgGuid", orgGuid)
        .bind("orgRoleGuid", orgRoleGuid)
        .bind("accountGuid", accountGuid)
        .updateOnly() ?: throw OrgRoleMembershipNotFound()
    }

  private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isForeignKeyViolation(FK_ORG_ROLE_GUID) -> throw OrgRoleNotFound().unprocessable()
      error.isUniqueConstraintViolation(UNIQ_ACCOUNT_GUID) -> throw AccountIsAlreadyMemberOfOrgRole()
      else -> throw e
    }
  }
}
