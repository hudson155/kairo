package limber.store.organization

import limber.exception.organization.OrganizationDoesNotExist
import limber.feature.sql.SqlStore
import limber.model.organization.OrganizationModel
import limber.util.updater.Updater
import mu.KLogger
import mu.KotlinLogging
import org.jdbi.v3.core.kotlin.bindKotlin

internal class OrganizationStore : SqlStore<OrganizationModel>(
  tableName = "organization.organization",
  type = OrganizationModel::class,
) {
  private val logger: KLogger = KotlinLogging.logger {}

  suspend fun listAll(): List<OrganizationModel> =
    sql { handle ->
      val query = handle.createQuery(rs("store/organization/listAll.sql"))
      return@sql query.mapToType().toList()
    }

  suspend fun search(search: String): List<OrganizationModel> =
    sql { handle ->
      val query = handle.createQuery(rs("store/organization/search.sql"))
      query.bind("search", search)
      return@sql query.mapToType().toList()
    }

  suspend fun create(creator: OrganizationModel.Creator): OrganizationModel =
    sql { handle ->
      logger.info { "Creating organization: $creator." }
      val query = handle.createQuery(rs("store/organization/create.sql"))
      query.bindKotlin(creator)
      return@sql query.mapToType().single()
    }

  suspend fun update(id: String, updater: Updater<OrganizationModel.Update>): OrganizationModel =
    sql { handle ->
      val organization = get(id, forUpdate = true) ?: throw OrganizationDoesNotExist()
      val update = updater(OrganizationModel.Update(organization))
      logger.info { "Updating organization: $update." }
      val query = handle.createQuery(rs("store/organization/update.sql"))
      query.bind("id", id)
      query.bindKotlin(update)
      return@sql query.mapToType().single()
    }

  suspend fun delete(id: String): OrganizationModel =
    sql { handle ->
      logger.info { "Deleting organization." }
      val query = handle.createQuery(rs("store/organization/delete.sql"))
      query.bind("id", id)
      return@sql query.mapToType().singleNullOrThrow() ?: throw OrganizationDoesNotExist()
    }
}
