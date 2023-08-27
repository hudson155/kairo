package limber.store.organizationHostname

import limber.exception.organization.OrganizationDoesNotExist
import limber.exception.organizationHostname.OrganizationHostnameAlreadyTaken
import limber.exception.organizationHostname.OrganizationHostnameDoesNotExist
import limber.feature.sql.SqlStore
import limber.feature.sql.isForeignKeyViolation
import limber.feature.sql.isUniqueViolation
import limber.model.organizationHostname.OrganizationHostnameModel
import mu.KLogger
import mu.KotlinLogging
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.postgresql.util.ServerErrorMessage

internal class OrganizationHostnameStore : SqlStore<OrganizationHostnameModel>(
  tableName = "organization.organization_hostname",
  type = OrganizationHostnameModel::class,
) {
  private val logger: KLogger = KotlinLogging.logger {}

  suspend fun listByOrganization(organizationId: String): List<OrganizationHostnameModel> =
    sql { handle ->
      val query = handle.createQuery(rs("store/organizationHostname/listByOrganization.sql"))
      query.bind("organizationId", organizationId)
      return@sql query.mapToType().toList()
    }

  suspend fun create(creator: OrganizationHostnameModel.Creator): OrganizationHostnameModel =
    sql { handle ->
      logger.info { "Creating organization hostname: $creator." }
      val query = handle.createQuery(rs("store/organizationHostname/create.sql"))
      query.bindKotlin(creator)
      return@sql query.mapToType().single()
    }

  suspend fun delete(id: String): OrganizationHostnameModel =
    sql { handle ->
      logger.info { "Deleting organization hostname." }
      val query = handle.createQuery(rs("store/organizationHostname/delete.sql"))
      query.bind("id", id)
      return@sql query.mapToType().singleNullOrThrow() ?: throw OrganizationHostnameDoesNotExist()
    }

  override fun ServerErrorMessage.onError(e: UnableToExecuteStatementException) {
    when {
      isForeignKeyViolation("fk__organization_hostname__organization_id") ->
        throw OrganizationDoesNotExist()
      isUniqueViolation("uq__organization_hostname__hostname") ->
        throw OrganizationHostnameAlreadyTaken()
    }
  }
}
