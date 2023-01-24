package limber.store.organizationHostname

import limber.exception.organization.OrganizationDoesNotExist
import limber.exception.organizationHostname.OrganizationHostnameAlreadyTaken
import limber.exception.organizationHostname.OrganizationHostnameDoesNotExist
import limber.feature.sql.SqlStore
import limber.feature.sql.isForeignKeyViolation
import limber.feature.sql.isUniqueViolation
import limber.rep.organizationHostname.OrganizationHostnameRep
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.postgresql.util.ServerErrorMessage
import java.util.UUID

internal class OrganizationHostnameStore : SqlStore<OrganizationHostnameRep>(OrganizationHostnameRep::class) {
  fun get(guid: UUID): OrganizationHostnameRep? =
    handle { handle ->
      val query = handle.createQuery(rs("store/organizationHostname/get.sql"))
      query.bind("guid", guid)
      return@handle query.mapToType().singleNullOrThrow()
    }

  fun create(model: OrganizationHostnameRep): OrganizationHostnameRep =
    transaction { handle ->
      val query = handle.createQuery(rs("store/organizationHostname/create.sql"))
      query.bindKotlin(model)
      return@transaction query.mapToType().single()
    }

  fun delete(guid: UUID): OrganizationHostnameRep =
    transaction { handle ->
      val query = handle.createQuery(rs("store/organizationHostname/delete.sql"))
      query.bind("guid", guid)
      return@transaction query.mapToType().singleNullOrThrow() ?: throw OrganizationHostnameDoesNotExist()
    }

  override fun ServerErrorMessage.onError(e: UnableToExecuteStatementException) {
    when {
      isForeignKeyViolation("fk__organization_hostname__organization_guid") ->
        throw OrganizationDoesNotExist()
      isUniqueViolation("uq__organization_hostname__hostname") ->
        throw OrganizationHostnameAlreadyTaken()
    }
  }
}
