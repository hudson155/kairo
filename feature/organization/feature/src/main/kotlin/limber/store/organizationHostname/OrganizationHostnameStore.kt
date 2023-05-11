package limber.store.organizationHostname

import limber.exception.organization.OrganizationDoesNotExist
import limber.exception.organizationHostname.OrganizationHostnameAlreadyTaken
import limber.exception.organizationHostname.OrganizationHostnameDoesNotExist
import limber.feature.sql.SqlStore
import limber.feature.sql.isForeignKeyViolation
import limber.feature.sql.isUniqueViolation
import limber.model.organizationHostname.OrganizationHostnameModel
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.postgresql.util.ServerErrorMessage
import java.util.UUID

internal class OrganizationHostnameStore : SqlStore<OrganizationHostnameModel>(
  tableName = "organization.organization_hostname",
  type = OrganizationHostnameModel::class,
) {
  fun create(hostname: OrganizationHostnameModel.Creator): OrganizationHostnameModel =
    transaction { handle ->
      val query = handle.createQuery(rs("store/organizationHostname/create.sql"))
      query.bindKotlin(hostname)
      return@transaction query.mapToType().single()
    }

  fun delete(guid: UUID): OrganizationHostnameModel =
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
