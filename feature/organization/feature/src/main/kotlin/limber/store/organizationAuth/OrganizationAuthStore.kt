package limber.store.organizationAuth

import limber.exception.organization.OrganizationDoesNotExist
import limber.exception.organizationAuth.Auth0OrganizationIdAlreadyTaken
import limber.exception.organizationAuth.Auth0OrganizationNameAlreadyTaken
import limber.exception.organizationAuth.OrganizationAlreadyHasOrganizationAuth
import limber.exception.organizationAuth.OrganizationAuthDoesNotExist
import limber.feature.sql.SqlStore
import limber.feature.sql.isForeignKeyViolation
import limber.feature.sql.isUniqueViolation
import limber.model.organizationAuth.OrganizationAuthModel
import limber.util.updater.Updater
import mu.KLogger
import mu.KotlinLogging
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.postgresql.util.ServerErrorMessage
import java.util.UUID

internal class OrganizationAuthStore : SqlStore<OrganizationAuthModel>(
  tableName = "organization.organization_auth",
  type = OrganizationAuthModel::class,
) {
  private val logger: KLogger = KotlinLogging.logger {}

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

  fun create(creator: OrganizationAuthModel.Creator): OrganizationAuthModel =
    transaction { handle ->
      logger.info { "Creating organization auth: $creator." }
      val query = handle.createQuery(rs("store/organizationAuth/create.sql"))
      query.bindKotlin(creator)
      return@transaction query.mapToType().single()
    }

  fun update(guid: UUID, updater: Updater<OrganizationAuthModel.Update>): OrganizationAuthModel =
    transaction { handle ->
      val auth = get(guid, forUpdate = true) ?: throw OrganizationAuthDoesNotExist()
      val update = updater(OrganizationAuthModel.Update(auth))
      logger.info { "Updating organization auth: $update." }
      val query = handle.createQuery(rs("store/organizationAuth/update.sql"))
      query.bind("guid", guid)
      query.bindKotlin(update)
      return@transaction query.mapToType().single()
    }

  fun delete(guid: UUID): OrganizationAuthModel =
    transaction { handle ->
      logger.info { "Deleting organization auth." }
      val query = handle.createQuery(rs("store/organizationAuth/delete.sql"))
      query.bind("guid", guid)
      return@transaction query.mapToType().singleNullOrThrow() ?: throw OrganizationAuthDoesNotExist()
    }

  override fun ServerErrorMessage.onError(e: UnableToExecuteStatementException) {
    when {
      isForeignKeyViolation("fk__organization_auth__organization_guid") ->
        throw OrganizationDoesNotExist()
      isUniqueViolation("uq__organization_auth__organization_guid") ->
        throw OrganizationAlreadyHasOrganizationAuth()
      isUniqueViolation("uq__organization_auth__auth0_organization_id") ->
        throw Auth0OrganizationIdAlreadyTaken()
      isUniqueViolation("uq__organization_auth__auth0_organization_name") ->
        throw Auth0OrganizationNameAlreadyTaken()
    }
  }
}
