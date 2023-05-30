package limber.store.organization

import limber.exception.organization.OrganizationDoesNotExist
import limber.feature.sql.SqlStore
import limber.model.organization.OrganizationModel
import limber.util.updater.Updater
import mu.KLogger
import mu.KotlinLogging
import org.jdbi.v3.core.kotlin.bindKotlin
import java.util.UUID

internal class OrganizationStore : SqlStore<OrganizationModel>(
  tableName = "organization.organization",
  type = OrganizationModel::class,
) {
  private val logger: KLogger = KotlinLogging.logger {}

  fun listAll(): List<OrganizationModel> =
    handle { handle ->
      val query = handle.createQuery(rs("store/organization/listAll.sql"))
      return@handle query.mapToType().toList()
    }

  fun search(search: String): List<OrganizationModel> =
    handle { handle ->
      val query = handle.createQuery(rs("store/organization/search.sql"))
      query.bind("search", search)
      return@handle query.mapToType().toList()
    }

  fun create(creator: OrganizationModel.Creator): OrganizationModel =
    transaction { handle ->
      logger.info { "Creating organization: $creator." }
      val query = handle.createQuery(rs("store/organization/create.sql"))
      query.bindKotlin(creator)
      return@transaction query.mapToType().single()
    }

  fun update(guid: UUID, updater: Updater<OrganizationModel.Update>): OrganizationModel =
    transaction { handle ->
      val organization = getByGuid(guid, forUpdate = true) ?: throw OrganizationDoesNotExist()
      val update = updater(OrganizationModel.Update(organization))
      logger.info { "Updating organization: $update." }
      val query = handle.createQuery(rs("store/organization/update.sql"))
      query.bind("guid", guid)
      query.bindKotlin(update)
      return@transaction query.mapToType().single()
    }
}
