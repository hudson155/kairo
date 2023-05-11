package limber.store.organization

import limber.exception.organization.OrganizationDoesNotExist
import limber.feature.sql.SqlStore
import limber.model.organization.OrganizationModel
import limber.util.updater.Updater
import org.jdbi.v3.core.kotlin.bindKotlin
import java.util.UUID

internal class OrganizationStore : SqlStore<OrganizationModel>(
  tableName = "organization.organization",
  type = OrganizationModel::class,
) {
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

  fun create(organization: OrganizationModel.Creator): OrganizationModel =
    transaction { handle ->
      val query = handle.createQuery(rs("store/organization/create.sql"))
      query.bindKotlin(organization)
      return@transaction query.mapToType().single()
    }

  fun update(guid: UUID, updater: Updater<OrganizationModel.Update>): OrganizationModel =
    transaction { handle ->
      val organization = get(guid, forUpdate = true) ?: throw OrganizationDoesNotExist()
      val query = handle.createQuery(rs("store/organization/update.sql"))
      query.bind("guid", guid)
      query.bindKotlin(updater(OrganizationModel.Update(organization)))
      return@transaction query.mapToType().single()
    }
}
