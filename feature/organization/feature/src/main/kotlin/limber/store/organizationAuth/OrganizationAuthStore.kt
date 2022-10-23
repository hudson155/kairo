package limber.store.organizationAuth

import limber.exception.ConflictException
import limber.exception.UnprocessableException
import limber.feature.sql.SqlStore
import limber.feature.sql.isForeignKeyViolation
import limber.feature.sql.isUniqueViolation
import limber.rep.organizationAuth.OrganizationAuthRep
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.postgresql.util.ServerErrorMessage
import java.util.UUID

internal class OrganizationAuthStore : SqlStore<OrganizationAuthRep>(OrganizationAuthRep::class) {
  fun set(model: OrganizationAuthRep): OrganizationAuthRep =
    transaction { handle ->
      val query = handle.createQuery(rs("store/organizationAuth/set.sql"))
      query.bindKotlin(model)
      return@transaction query.mapToType().single()
    }

  fun getByOrganization(organizationGuid: UUID): OrganizationAuthRep? =
    handle { handle ->
      val query = handle.createQuery(rs("store/organizationAuth/getByOrganization.sql"))
      query.bind("organizationGuid", organizationGuid)
      return@handle query.mapToType().singleNullOrThrow()
    }

  fun deleteByOrganization(organizationGuid: UUID): OrganizationAuthRep =
    transaction { handle ->
      val query = handle.createQuery(rs("store/organizationAuth/deleteByOrganization.sql"))
      query.bind("organizationGuid", organizationGuid)
      return@transaction query.mapToType().singleNullOrThrow() ?: authDoesNotExist()
    }

  override fun ServerErrorMessage.onError(e: UnableToExecuteStatementException) {
    when {
      isForeignKeyViolation("fk__organization_auth__organization_guid") ->
        organizationDoesNotExist()
      isUniqueViolation("uq__organization_auth__auth0_organization_id") ->
        auth0OrganizationIdAlreadyTaken()
    }
  }

  private fun organizationDoesNotExist(): Nothing =
    throw UnprocessableException("Organization does not exist.")

  private fun authDoesNotExist(): Nothing =
    throw UnprocessableException("Auth does not exist.")

  private fun auth0OrganizationIdAlreadyTaken(): Nothing =
    throw ConflictException("Auth0 organization ID already taken.")
}
