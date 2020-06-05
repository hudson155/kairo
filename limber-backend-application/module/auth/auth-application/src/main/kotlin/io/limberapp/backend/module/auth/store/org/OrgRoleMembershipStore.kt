package io.limberapp.backend.module.auth.store.org

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.store.isUniqueConstraintViolation
import io.limberapp.backend.module.auth.exception.org.AccountIsAlreadyMemberOfOrgRole
import io.limberapp.backend.module.auth.exception.org.OrgRoleMembershipNotFound
import io.limberapp.backend.module.auth.model.org.OrgRoleMembershipModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*

private const val ORG_ROLE_GUID_ACCOUNT_GUID_UNIQUE_CONSTRAINT = "org_role_membership_org_role_guid_account_guid_key"

internal class OrgRoleMembershipStore @Inject constructor(private val jdbi: Jdbi) : SqlStore() {
  fun create(model: OrgRoleMembershipModel): OrgRoleMembershipModel {
    return jdbi.withHandle<OrgRoleMembershipModel, Exception> {
      try {
        it.createQuery(sqlResource("create"))
          .bindKotlin(model)
          .mapTo(OrgRoleMembershipModel::class.java)
          .single()
      } catch (e: UnableToExecuteStatementException) {
        handleCreateError(e)
      }
    }
  }

  private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    if (error.isUniqueConstraintViolation(ORG_ROLE_GUID_ACCOUNT_GUID_UNIQUE_CONSTRAINT)) {
      throw AccountIsAlreadyMemberOfOrgRole()
    }
    throw e
  }

  fun getByOrgRoleGuid(orgRoleGuid: UUID): Set<OrgRoleMembershipModel> {
    return jdbi.withHandle<Set<OrgRoleMembershipModel>, Exception> {
      it.createQuery("SELECT * FROM auth.org_role_membership WHERE org_role_guid = :orgRoleGuid")
        .bind("orgRoleGuid", orgRoleGuid)
        .mapTo(OrgRoleMembershipModel::class.java)
        .toSet()
    }
  }

  fun delete(orgRoleGuid: UUID, accountGuid: UUID) {
    jdbi.useTransaction<Exception> {
      val updateCount = it.createUpdate(
          """
                    DELETE
                    FROM auth.org_role_membership
                    WHERE org_role_guid = :orgRoleGuid
                      AND account_guid = :accountGuid
                    """.trimIndent()
        )
        .bind("orgRoleGuid", orgRoleGuid)
        .bind("accountGuid", accountGuid)
        .execute()
      return@useTransaction when (updateCount) {
        0 -> throw OrgRoleMembershipNotFound()
        1 -> Unit
        else -> badSql()
      }
    }
  }
}
