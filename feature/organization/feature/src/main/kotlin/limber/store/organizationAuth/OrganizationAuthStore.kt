package limber.store.organizationAuth

import limber.exception.organization.OrganizationDoesNotExist
import limber.exception.organizationAuth.Auth0OrganizationIdAlreadyTaken
import limber.exception.organizationAuth.OrganizationAuthDoesNotExist
import limber.feature.sql.SqlStore
import limber.feature.sql.isForeignKeyViolation
import limber.feature.sql.isUniqueViolation
import limber.model.organizationAuth.OrganizationAuthModel
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.postgresql.util.ServerErrorMessage
import java.util.UUID

internal class OrganizationAuthStore : SqlStore<OrganizationAuthModel>(
  tableName = "organization.organization_auth",
  type = OrganizationAuthModel::class,
) {
  fun getByOrganization(organizationGuid: UUID): OrganizationAuthModel? =
    handle { handle ->
      val query = handle.createQuery(rs("store/organizationAuth/getByOrganization.sql"))
      query.bind("organizationGuid", organizationGuid)
      return@handle query.mapToType().singleNullOrThrow()
    }

  fun getByHostname(hostname: String): OrganizationAuthModel? =
    handle { handle ->
      val query = handle.createQuery(rs("store/organizationAuth/getByHostname.sql"))
      query.bind("hostname", hostname)
      return@handle query.mapToType().singleNullOrThrow()
    }

  fun set(model: OrganizationAuthModel.Creator): OrganizationAuthModel =
    transaction { handle ->
      val query = handle.createQuery(rs("store/organizationAuth/set.sql"))
      query.bindKotlin(model)
      return@transaction query.mapToType().single()
    }

  fun deleteByOrganization(organizationGuid: UUID): OrganizationAuthModel =
    transaction { handle ->
      val query = handle.createQuery(rs("store/organizationAuth/deleteByOrganization.sql"))
      query.bind("organizationGuid", organizationGuid)
      return@transaction query.mapToType().singleNullOrThrow() ?: throw OrganizationAuthDoesNotExist()
    }

  override fun ServerErrorMessage.onError(e: UnableToExecuteStatementException) {
    when {
      isForeignKeyViolation("fk__organization_auth__organization_guid") ->
        throw OrganizationDoesNotExist()
      isUniqueViolation("uq__organization_auth__auth0_organization_id") ->
        throw Auth0OrganizationIdAlreadyTaken()
    }
  }
}
