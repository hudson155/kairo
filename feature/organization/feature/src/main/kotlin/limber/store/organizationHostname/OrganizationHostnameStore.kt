package limber.store.organizationHostname

import limber.exception.ConflictException
import limber.exception.UnprocessableException
import limber.feature.sql.SqlStore
import limber.feature.sql.isForeignKeyViolation
import limber.feature.sql.isUniqueViolation
import limber.rep.organizationHostname.OrganizationHostnameRep
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.postgresql.util.ServerErrorMessage
import java.util.UUID

internal class OrganizationHostnameStore : SqlStore<OrganizationHostnameRep>(OrganizationHostnameRep::class) {
  fun create(model: OrganizationHostnameRep): OrganizationHostnameRep =
    transaction { handle ->
      val query = handle.createQuery(rs("store/organizationHostname/create.sql"))
      query.bindKotlin(model)
      return@transaction query.mapToType().single()
    }

  fun get(organizationGuid: UUID, guid: UUID): OrganizationHostnameRep? =
    handle { handle ->
      val query = handle.createQuery(rs("store/organizationHostname/get.sql"))
      query.bind("organizationGuid", organizationGuid)
      query.bind("guid", guid)
      return@handle query.mapToType().singleNullOrThrow()
    }

  fun delete(organizationGuid: UUID, guid: UUID): OrganizationHostnameRep =
    transaction { handle ->
      val query = handle.createQuery(rs("store/organizationHostname/delete.sql"))
      query.bind("organizationGuid", organizationGuid)
      query.bind("guid", guid)
      return@transaction query.mapToType().singleNullOrThrow() ?: hostnameDoesNotExist()
    }

  override fun ServerErrorMessage.onError(e: UnableToExecuteStatementException) {
    when {
      isForeignKeyViolation("fk__organization_hostname__organization_guid") ->
        organizationDoesNotExist()
      isUniqueViolation("uq__organization_hostname__hostname") ->
        hostnameAlreadyTaken()
    }
  }

  private fun organizationDoesNotExist(): Nothing =
    throw UnprocessableException("Organization does not exist.")

  private fun hostnameDoesNotExist(): Nothing =
    throw UnprocessableException("Hostname does not exist.")

  private fun hostnameAlreadyTaken(): Nothing =
    throw ConflictException("Hostname already taken.")
}
